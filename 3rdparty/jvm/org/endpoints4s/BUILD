load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "algebra",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":algebra_json_schema"
    ],
    jars = [
        "//external:jar/org/endpoints4s/algebra_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "algebra_circe",
    exports = [
        "//3rdparty/jvm/io/circe:circe_parser",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":algebra"
    ],
    jars = [
        "//external:jar/org/endpoints4s/algebra_circe_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "algebra_json_schema",
    exports = [
        "//3rdparty/jvm/org/scala_lang/modules:scala_collection_compat",
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/endpoints4s/algebra_json_schema_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "http4s_server",
    exports = [
        "//3rdparty/jvm/org/http4s:http4s_core",
        "//3rdparty/jvm/org/http4s:http4s_dsl",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":algebra",
        ":openapi"
    ],
    jars = [
        "//external:jar/org/endpoints4s/http4s_server_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "json_schema_circe",
    exports = [
        "//3rdparty/jvm/io/circe:circe_core",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":algebra_circe",
        ":algebra_json_schema"
    ],
    jars = [
        "//external:jar/org/endpoints4s/json_schema_circe_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "json_schema_generic",
    exports = [
        "//3rdparty/jvm/com/chuusai:shapeless",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":algebra_json_schema"
    ],
    jars = [
        "//external:jar/org/endpoints4s/json_schema_generic_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "openapi",
    exports = [
        "//3rdparty/jvm/com/lihaoyi:ujson_2_13",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":algebra",
        ":algebra_json_schema"
    ],
    jars = [
        "//external:jar/org/endpoints4s/openapi_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


