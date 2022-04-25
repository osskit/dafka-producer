public class Main {

    static Config config;
    static Monitor monitor;
    static Producer producer;
    static Server server;

    public static void main(String[] args) throws Exception {
        Config.init();
        Monitor.init();
        producer = new Producer(config, monitor);
        server = new Server(config, monitor, producer);
        server.start();
        producer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> close()));
        Monitor.started();
    }

    private static void close() {
        producer.close();
        server.close();
        Monitor.serviceShutdown();
    }
}
