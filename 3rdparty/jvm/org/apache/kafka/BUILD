load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "kafka_clients",
    exports = [
        "//3rdparty/jvm/com/github/luben:zstd_jni",
        "//3rdparty/jvm/org/lz4:lz4_java",
        "//3rdparty/jvm/org/slf4j:slf4j_api",
        "//3rdparty/jvm/org/xerial/snappy:snappy_java",
        "//external:jar/org/apache/kafka/kafka_clients"
    ],
    visibility = [
        "//visibility:public"
    ]
)


