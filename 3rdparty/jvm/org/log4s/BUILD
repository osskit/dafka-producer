load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "log4s",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/slf4j:slf4j_api"
    ],
    jars = [
        "//external:jar/org/log4s/log4s_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


