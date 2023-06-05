load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "common_utils",
    exports = [
        "//3rdparty/jvm/org/slf4j:slf4j_api",
        "//external:jar/io/confluent/common_utils"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "kafka_avro_serializer",
    exports = [
        "//3rdparty/jvm/com/google/guava:guava",
        "//3rdparty/jvm/org/apache/avro:avro",
        "//3rdparty/jvm/org/apache/commons:commons_compress",
        "//external:jar/io/confluent/kafka_avro_serializer",
        ":common_utils",
        ":kafka_schema_registry_client",
        ":kafka_schema_serializer",
        ":logredactor"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "kafka_schema_registry_client",
    exports = [
        "//3rdparty/jvm/com/fasterxml/jackson/core:jackson_databind",
        "//3rdparty/jvm/com/google/guava:guava",
        "//3rdparty/jvm/io/swagger/core/v3:swagger_annotations",
        "//3rdparty/jvm/org/apache/avro:avro",
        "//3rdparty/jvm/org/apache/commons:commons_compress",
        "//3rdparty/jvm/org/apache/kafka:kafka_clients",
        "//3rdparty/jvm/org/yaml:snakeyaml",
        "//external:jar/io/confluent/kafka_schema_registry_client",
        ":common_utils"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "kafka_schema_serializer",
    exports = [
        "//external:jar/io/confluent/kafka_schema_serializer",
        ":common_utils",
        ":kafka_schema_registry_client"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "logredactor",
    exports = [
        "//3rdparty/jvm/com/eclipsesource/minimal_json:minimal_json",
        "//3rdparty/jvm/com/google/re2j:re2j",
        "//external:jar/io/confluent/logredactor",
        ":logredactor_metrics"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "logredactor_metrics",
    exports = [
        "//external:jar/io/confluent/logredactor_metrics"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


