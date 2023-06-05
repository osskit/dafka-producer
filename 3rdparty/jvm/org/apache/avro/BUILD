load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "avro",
    exports = [
        "//3rdparty/jvm/com/fasterxml/jackson/core:jackson_core",
        "//3rdparty/jvm/com/fasterxml/jackson/core:jackson_databind",
        "//3rdparty/jvm/org/apache/commons:commons_compress",
        "//3rdparty/jvm/org/slf4j:slf4j_api",
        "//external:jar/org/apache/avro/avro"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


