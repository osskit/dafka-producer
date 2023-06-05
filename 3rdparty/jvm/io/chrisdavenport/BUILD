load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "epimetheus",
    exports = [
        "//3rdparty/jvm/com/chuusai:shapeless",
        "//3rdparty/jvm/io/prometheus:simpleclient",
        "//3rdparty/jvm/io/prometheus:simpleclient_common",
        "//3rdparty/jvm/io/prometheus:simpleclient_hotspot",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_lang:scala_reflect",
        "//3rdparty/jvm/org/typelevel:cats_core",
        "//3rdparty/jvm/org/typelevel:cats_effect"
    ],
    jars = [
        "//external:jar/io/chrisdavenport/epimetheus_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


