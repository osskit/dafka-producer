load("@io_bazel_rules_scala//scala:scala.bzl", "scala_binary", "scala_library", "scala_test")
load("@io_bazel_rules_docker//scala:image.bzl", "scala_image")

scala_library(
    name = "lib",
    srcs = glob(["main/scala/**/*.scala"]),
    deps = [
        "@third_party//3rdparty/jvm/ch/qos/logback:logback_classic",
        "@third_party//3rdparty/jvm/ch/qos/logback:logback_core",
        "@third_party//3rdparty/jvm/ch/qos/logback/contrib:logback_jackson",
        "@third_party//3rdparty/jvm/ch/qos/logback/contrib:logback_json_classic",
        "@third_party//3rdparty/jvm/com/banno:kafka4s",
        "@third_party//3rdparty/jvm/com/beachape:enumeratum",
        "@third_party//3rdparty/jvm/com/fasterxml/jackson/core:jackson_core",
        "@third_party//3rdparty/jvm/com/fasterxml/jackson/core:jackson_databind",
        "@third_party//3rdparty/jvm/eu/timepit:refined",
        "@third_party//3rdparty/jvm/io/circe:circe_core",
        "@third_party//3rdparty/jvm/io/circe:circe_generic",
        "@third_party//3rdparty/jvm/is/cir:ciris",
        "@third_party//3rdparty/jvm/is/cir:ciris_enumeratum",
        "@third_party//3rdparty/jvm/is/cir:ciris_refined",
        "@third_party//3rdparty/jvm/org/endpoints4s:algebra",
        "@third_party//3rdparty/jvm/org/endpoints4s:http4s_server",
        "@third_party//3rdparty/jvm/org/endpoints4s:json_schema_circe",
        "@third_party//3rdparty/jvm/org/endpoints4s:json_schema_generic",
        "@third_party//3rdparty/jvm/org/http4s:http4s_dsl",
        "@third_party//3rdparty/jvm/org/http4s:http4s_ember_server",
        "@third_party//3rdparty/jvm/org/http4s:http4s_prometheus_metrics",
        "@third_party//3rdparty/jvm/org/slf4j:slf4j_api",
        "@third_party//3rdparty/jvm/org/typelevel:log4cats_core",
        "@third_party//3rdparty/jvm/org/typelevel:log4cats_slf4j",
    ],
)

scala_binary(
    name = "main",
    srcs = [],
    main_class = "Main",
    resources = ["main/resources/logback.xml"],
    deps = [":lib"],
)

scala_image(
    name = "image",
    main_class = "Main",
    runtime_deps = [":lib"],
)
