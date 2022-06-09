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
import java.util.concurrent.Executors;
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
        producePostRoute(server);
        aliveRoute(server);
        readyRoute(server);
        server.setExecutor(Executors.newCachedThreadPool());

        if (Config.USE_PROMETHEUS) {
            DefaultExports.initialize();
            new HTTPServer(server, CollectorRegistry.defaultRegistry, false);
        } else {
            server.start();
        }
        Monitor.httpServerStarted();
        return this;
    }

    public void close() {
        server.stop(0);
    }

    private void aliveRoute(final HttpServer server) {
        final var httpContext = server.createContext("/alive");

        httpContext.setHandler(
            new HttpHandler() {
                @Override
                public void handle(final HttpExchange exchange) throws IOException {
                    System.out.println("calling /alive");
                    if (!exchange.getRequestMethod().equals("GET")) {
                        exchange.sendResponseHeaders(404, -1);
                        return;
                    }

                    exchange.sendResponseHeaders(204, -1);
                }
            }
        );
    }

    private void readyRoute(final HttpServer server) {
        final var httpContext = server.createContext("/ready");

        httpContext.setHandler(
            new HttpHandler() {
                @Override
                public void handle(final HttpExchange exchange) throws IOException {
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
                                        getHeaders(exchange.getRequestHeaders())
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

    private static String tryGetValue(JSONObject json, String key) {
        if (json.has(key)) {
            return json.get(key).toString();
        }
        throw new IllegalArgumentException(key + " is missing");
    }

    private RecordHeaders getHeaders(Headers headers) {
        var recordHeaders = new RecordHeaders();

        var requestId = headers.getFirst("x-request-id");
        if (requestId != null) {
            recordHeaders.add("x-request-id", requestId.getBytes());
        }
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
        var ceSpecVersion = headers.getFirst("ce-specversion");
        if (ceSpecVersion != null) {
            recordHeaders.add("ce_specversion", ceSpecVersion.getBytes());
        }
        var ceSource = headers.getFirst("ce-source");
        if (ceSource != null) {
            recordHeaders.add("ce_source", ceSource.getBytes());
        }
        var ceTime = headers.getFirst("ce-time");
        if (ceTime != null) {
            recordHeaders.add("ce_time", ceTime.getBytes());
        }
        var ceId = headers.getFirst("ce-id");
        if (ceId != null) {
            recordHeaders.add("ce_id", ceId.getBytes());
        }
        var ceType = headers.getFirst("ce-type");
        if (ceType != null) {
            recordHeaders.add("ce_type", ceType.getBytes());
        }

        return recordHeaders;
    }
}
