load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "kafka4s",
    exports = [
        "//3rdparty/jvm/co/fs2:fs2_core",
        "//3rdparty/jvm/io/chrisdavenport:epimetheus",
        "//3rdparty/jvm/io/confluent:kafka_avro_serializer",
        "//3rdparty/jvm/org/apache/kafka:kafka_clients",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/typelevel:cats_effect",
        "//3rdparty/jvm/org/typelevel:log4cats_slf4j",
        "//3rdparty/jvm/org/typelevel:scalacheck_effect_munit"
    ],
    jars = [
        "//external:jar/com/banno/kafka4s_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)


