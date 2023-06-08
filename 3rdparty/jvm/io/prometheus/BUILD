load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "simpleclient",
    exports = [
        "//external:jar/io/prometheus/simpleclient",
        ":simpleclient_tracer_otel",
        ":simpleclient_tracer_otel_agent"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "simpleclient_common",
    exports = [
        "//external:jar/io/prometheus/simpleclient_common",
        ":simpleclient"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "simpleclient_hotspot",
    exports = [
        "//external:jar/io/prometheus/simpleclient_hotspot",
        ":simpleclient"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "simpleclient_tracer_common",
    exports = [
        "//external:jar/io/prometheus/simpleclient_tracer_common"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "simpleclient_tracer_otel",
    exports = [
        "//external:jar/io/prometheus/simpleclient_tracer_otel",
        ":simpleclient_tracer_common"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "simpleclient_tracer_otel_agent",
    exports = [
        "//external:jar/io/prometheus/simpleclient_tracer_otel_agent",
        ":simpleclient_tracer_common"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


