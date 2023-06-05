load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "scala_compiler",
    exports = [
        "//3rdparty/jvm/io/github/java_diff_utils:java_diff_utils",
        "//3rdparty/jvm/net/java/dev/jna:jna",
        "//3rdparty/jvm/org/jline:jline",
        ":scala_library",
        ":scala_reflect"
    ],
    jars = [
        "//external:jar/org/scala_lang/scala_compiler"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "scala_library",
    jars = [
        "//external:jar/org/scala_lang/scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "scala_reflect",
    exports = [
        ":scala_library"
    ],
    jars = [
        "//external:jar/org/scala_lang/scala_reflect"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


