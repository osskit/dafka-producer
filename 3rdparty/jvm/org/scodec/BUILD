load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "scodec_bits",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/scodec/scodec_bits_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


