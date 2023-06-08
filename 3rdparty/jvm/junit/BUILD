load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "junit",
    exports = [
        "//3rdparty/jvm/org/hamcrest:hamcrest_core",
        "//external:jar/junit/junit"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


