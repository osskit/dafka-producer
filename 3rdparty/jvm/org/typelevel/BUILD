load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "case_insensitive",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_core"
    ],
    jars = [
        "//external:jar/org/typelevel/case_insensitive_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "cats_core",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_kernel"
    ],
    jars = [
        "//external:jar/org/typelevel/cats_core_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "cats_effect",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_effect_kernel",
        ":cats_effect_std"
    ],
    jars = [
        "//external:jar/org/typelevel/cats_effect_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "cats_effect_kernel",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_core"
    ],
    jars = [
        "//external:jar/org/typelevel/cats_effect_kernel_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "cats_effect_std",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_effect_kernel"
    ],
    jars = [
        "//external:jar/org/typelevel/cats_effect_std_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "cats_kernel",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/typelevel/cats_kernel_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "cats_parse",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_lang:scala_reflect"
    ],
    jars = [
        "//external:jar/org/typelevel/cats_parse_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "jawn_parser",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/typelevel/jawn_parser_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "literally",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/typelevel/literally_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "log4cats_core",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_core",
        ":cats_effect_std"
    ],
    jars = [
        "//external:jar/org/typelevel/log4cats_core_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "log4cats_slf4j",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/slf4j:slf4j_api",
        ":cats_effect",
        ":log4cats_core"
    ],
    jars = [
        "//external:jar/org/typelevel/log4cats_slf4j_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "scalacheck_effect",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scalacheck:scalacheck",
        ":cats_core"
    ],
    jars = [
        "//external:jar/org/typelevel/scalacheck_effect_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "scalacheck_effect_munit",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scalameta:munit_scalacheck",
        ":scalacheck_effect"
    ],
    jars = [
        "//external:jar/org/typelevel/scalacheck_effect_munit_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "vault",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":cats_core",
        ":cats_effect"
    ],
    jars = [
        "//external:jar/org/typelevel/vault_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


