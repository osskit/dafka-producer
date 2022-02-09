import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server {

    Config config;
    Monitor monitor;
    HttpServer server;
    Producer producer;

    Server(Config config, Monitor monitor, Producer producer) {
        this.config = config;
        this.monitor = monitor;
        this.producer = producer;
    }

    public Server start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(Config.PORT), 0);
        isAliveGetRoute(server);
        producePostRoute(server);
        if (Config.USE_PROMETHEUS) {
            DefaultExports.initialize();
            new HTTPServer(server, CollectorRegistry.defaultRegistry, false);
        } else {
            server.start();
        }
        return this;
    }

    public void close() {
        server.stop(0);
    }

    private void isAliveGetRoute(HttpServer server) {
        var httpContext = server.createContext("/isAlive");
        httpContext.setHandler(
            new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if (!exchange.getRequestMethod().equals("GET")) {
                        exchange.sendResponseHeaders(404, -1);
                        return;
                    }
                    if (!producer.ready()) {
                        exchange.sendResponseHeaders(500, -1);
                        return;
                    }
                    exchange.sendResponseHeaders(204, -1);
                }
            }
        );
    }

    private void producePostRoute(HttpServer server) {
        var httpContext = server.createContext("/produce");
        httpContext.setHandler(
            new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if (!exchange.getRequestMethod().equals("POST")) {
                        exchange.sendResponseHeaders(404, -1);
                        return;
                    }

                    try {
                        validateCorrelationIdHeader(exchange);

                        var body = CharStreams.toString(
                            new InputStreamReader(exchange.getRequestBody(), Charsets.UTF_8)
                        );
                        StreamSupport
                            .stream(new JSONArray(body).spliterator(), false)
                            .map(
                                jsonItem -> {
                                    var item = new JSONObject(jsonItem.toString());
                                    return new ProducerRequest(
                                        tryGetValue(item, "topic"),
                                        tryGetValue(item, "key"),
                                        tryGetValue(item, "value"),
                                        createRecordHeaders(exchange.getRequestHeaders())
                                    );
                                }
                            )
                            .map(producerRequest -> producer.produce(producerRequest))
                            .collect(Collectors.toList());

                        exchange.sendResponseHeaders(204, -1);
                    } catch (Exception e) {
                        var response = e.getMessage();
                        exchange.sendResponseHeaders(
                            e instanceof IllegalArgumentException ? 400 : 500,
                            response.getBytes().length
                        );
                        var os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                }
            }
        );
    }

    public String validateCorrelationIdHeader(HttpExchange exchange) {
        if (!Config.ENFORCE_CORRELATION_ID) {
            return null;
        }

        var correlationId = exchange.getRequestHeaders().getFirst(Config.CORRELATION_ID_HEADER_KEY);
        if (correlationId == null) {
            throw new IllegalArgumentException("correlationId is missing");
        }

        return correlationId;
    }

    private static String tryGetValue(JSONObject json, String key) {
        if (json.has(key)) {
            return json.get(key).toString();
        }
        throw new IllegalArgumentException(key + " is missing");
    }

    private RecordHeaders createRecordHeaders(Headers headers) {
        var recordHeaders = new RecordHeaders();
        headers.forEach(
            (key, value) -> {
                if (key.toLowerCase().startsWith(Config.HEADERS_PREFIX)) {
                    recordHeaders.add(key, value.get(0).getBytes());
                }
            }
        );
        return recordHeaders;
    }
}
