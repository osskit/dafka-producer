load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "logback_jackson",
    exports = [
        "//external:jar/ch/qos/logback/contrib/logback_jackson",
        ":logback_json_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "logback_json_classic",
    exports = [
        "//3rdparty/jvm/ch/qos/logback:logback_classic",
        "//external:jar/ch/qos/logback/contrib/logback_json_classic",
        ":logback_json_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "logback_json_core",
    exports = [
        "//3rdparty/jvm/ch/qos/logback:logback_core",
        "//external:jar/ch/qos/logback/contrib/logback_json_core"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


