load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "circe_core",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/typelevel:cats_core",
        ":circe_numbers"
    ],
    jars = [
        "//external:jar/io/circe/circe_core_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "circe_generic",
    exports = [
        "//3rdparty/jvm/com/chuusai:shapeless",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":circe_core"
    ],
    jars = [
        "//external:jar/io/circe/circe_generic_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "circe_jawn",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/typelevel:jawn_parser",
        ":circe_core"
    ],
    jars = [
        "//external:jar/io/circe/circe_jawn_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "circe_numbers",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/io/circe/circe_numbers_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "circe_parser",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":circe_core",
        ":circe_jawn"
    ],
    jars = [
        "//external:jar/io/circe/circe_parser_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)


