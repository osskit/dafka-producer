load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "geny_2_13",
    exports = [
        "//external:jar/com/lihaoyi/geny_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "ujson_2_13",
    exports = [
        "//external:jar/com/lihaoyi/ujson_2_13",
        ":upickle_core_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "upickle_core_2_13",
    exports = [
        "//external:jar/com/lihaoyi/upickle_core_2_13",
        ":geny_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


