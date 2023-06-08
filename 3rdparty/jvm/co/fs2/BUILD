load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "fs2_core",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scodec:scodec_bits",
        "//3rdparty/jvm/org/typelevel:cats_core",
        "//3rdparty/jvm/org/typelevel:cats_effect"
    ],
    jars = [
        "//external:jar/co/fs2/fs2_core_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "fs2_io",
    exports = [
        "//3rdparty/jvm/com/comcast:ip4s_core",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":fs2_core"
    ],
    jars = [
        "//external:jar/co/fs2/fs2_io_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


