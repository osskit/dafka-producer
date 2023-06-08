load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "enumeratum",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":enumeratum_macros"
    ],
    jars = [
        "//external:jar/com/beachape/enumeratum_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "enumeratum_macros",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_lang:scala_reflect"
    ],
    jars = [
        "//external:jar/com/beachape/enumeratum_macros_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


