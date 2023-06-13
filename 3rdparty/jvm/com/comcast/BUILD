load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "ip4s_core",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/typelevel:cats_core",
        "//3rdparty/jvm/org/typelevel:cats_effect_kernel",
        "//3rdparty/jvm/org/typelevel:literally"
    ],
    jars = [
        "//external:jar/com/comcast/ip4s_core_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


