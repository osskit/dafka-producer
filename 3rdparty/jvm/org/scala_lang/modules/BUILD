load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "scala_collection_compat",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/scala_lang/modules/scala_collection_compat_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


