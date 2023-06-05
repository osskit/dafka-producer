load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "ciris",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/typelevel:cats_effect_kernel"
    ],
    jars = [
        "//external:jar/is/cir/ciris_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "ciris_enumeratum",
    exports = [
        "//3rdparty/jvm/com/beachape:enumeratum",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":ciris"
    ],
    jars = [
        "//external:jar/is/cir/ciris_enumeratum_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "ciris_refined",
    exports = [
        "//3rdparty/jvm/eu/timepit:refined",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":ciris"
    ],
    jars = [
        "//external:jar/is/cir/ciris_refined_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)


