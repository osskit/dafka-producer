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
                                        tracingHeaders(exchange.getRequestHeaders())
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

    private RecordHeaders tracingHeaders(Headers headers) {
        var recordHeaders = new RecordHeaders();

        var traceId = headers.getFirst("x-b3-traceid");
        if (traceId != null) {
            recordHeaders.add("x-b3-traceid", traceId.getBytes());
        }
        var spanId = headers.getFirst("x-b3-spanid");
        if (spanId != null) {
            recordHeaders.add("x-b3-spanid", spanId.getBytes());
        }
        var parentSpanId = headers.getFirst("x-b3-parentspanid");
        if (parentSpanId != null) {
            recordHeaders.add("x-b3-parentspanid", parentSpanId.getBytes());
        }
        var sampled = headers.getFirst("x-b3-sampled");
        if (sampled != null) {
            recordHeaders.add("x-b3-sampled", sampled.getBytes());
        }
        var flags = headers.getFirst("x-b3-flags");
        if (flags != null) {
            recordHeaders.add("x-b3-flags", flags.getBytes());
        }
        var spanContext = headers.getFirst("x-ot-span-context");
        if (spanContext != null) {
            recordHeaders.add("x-ot-span-context", spanContext.getBytes());
        }

        if (Config.ENFORCE_CORRELATION_ID) {
            var correlationId = headers.getFirst(Config.CORRELATION_ID_HEADER_KEY);
            if (correlationId != null) {
                recordHeaders.add(Config.CORRELATION_ID_HEADER_KEY, correlationId.getBytes());
            }
        }

        return recordHeaders;
    }
}
