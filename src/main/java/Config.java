import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Files;
import java.nio.file.Paths;

class Config {

    //Required
    public static int PORT;
    public static String KAFKA_BROKER;

    //Optional
    public static String READINESS_TOPIC;
    public static int LINGER_TIME_MS;
    public static String BATCH_SIZE;
    public static String COMPRESSION_TYPE;

    //Authentication
    public static boolean USE_SASL_AUTH;
    public static String SASL_USERNAME;
    public static String SASL_PASSWORD;
    public static String SASL_MECHANISM;
    public static String TRUSTSTORE_FILE_PATH;
    public static String TRUSTSTORE_PASSWORD;

    //Monitoring
    public static boolean USE_PROMETHEUS;
    public static String PROMETHEUS_BUCKETS;

    public static void init() throws Exception {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        PORT = getInt(dotenv, "PORT");
        KAFKA_BROKER = getString(dotenv, "KAFKA_BROKER");

        READINESS_TOPIC = getOptionalString(dotenv, "READINESS_TOPIC", null);
        LINGER_TIME_MS = getOptionalInt(dotenv, "LINGER_TIME_MS", 0);
        BATCH_SIZE = getOptionalString(dotenv, "BATCH_SIZE", null);
        COMPRESSION_TYPE = getOptionalString(dotenv, "COMPRESSION_TYPE", "none");

        USE_SASL_AUTH = getOptionalBool(dotenv, "USE_SASL_AUTH", false);
        if (USE_SASL_AUTH) {
            SASL_USERNAME = getString(dotenv, "SASL_USERNAME");
            SASL_PASSWORD = getStringValueOrFromFile(dotenv, "SASL_PASSWORD");
            SASL_MECHANISM = getOptionalString(dotenv, "SASL_MECHANISM", "PLAIN");
            TRUSTSTORE_FILE_PATH = getOptionalString(dotenv, "TRUSTSTORE_FILE_PATH", null);
            if (TRUSTSTORE_FILE_PATH != null) {
                TRUSTSTORE_PASSWORD = getStringValueOrFromFile(dotenv, "TRUSTSTORE_PASSWORD");
            }
        }

        USE_PROMETHEUS = getOptionalBool(dotenv, "USE_PROMETHEUS", false);
        PROMETHEUS_BUCKETS = getOptionalString(dotenv, PROMETHEUS_BUCKETS, "0.003,0.03,0.1,0.3,1.5,10");
    }

    private static String getString(Dotenv dotenv, String name) throws Exception {
        String value = dotenv.get(name);

        if (value == null) {
            throw new Exception("missing env var: " + name);
        }

        return value;
    }

    private static String getStringValueOrFromFile(Dotenv dotenv, String name) throws Exception {
        String value = dotenv.get(name);

        if (value != null) {
            return value;
        }

        String filePath = dotenv.get(name + "_FILE_PATH");

        if (filePath == null) {
            throw new Exception("missing env var: " + name + " or " + name + "_FILE_PATH");
        }

        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private static String getOptionalString(Dotenv dotenv, String name, String fallback) {
        try {
            return getString(dotenv, name);
        } catch (Exception e) {
            return fallback;
        }
    }

    private static int getInt(Dotenv dotenv, String name) {
        return Integer.parseInt(dotenv.get(name));
    }

    private static int getOptionalInt(Dotenv dotenv, String name, int fallback) {
        try {
            return Integer.parseInt(dotenv.get(name));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static boolean getOptionalBool(Dotenv dotenv, String name, boolean fallback) {
        try {
            return Boolean.parseBoolean(getString(dotenv, name));
        } catch (Exception e) {
            return fallback;
        }
    }
}
