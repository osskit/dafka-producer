load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
scala_import(
    name = "blaze_core",
    exports = [
        "//3rdparty/jvm/org/log4s:log4s",
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    jars = [
        "//external:jar/org/http4s/blaze_core_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "blaze_http",
    exports = [
        "//3rdparty/jvm/com/twitter:hpack",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":blaze_core"
    ],
    jars = [
        "//external:jar/org/http4s/blaze_http_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "http4s_blaze_core",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":blaze_http",
        ":http4s_core"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_blaze_core_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "http4s_blaze_server",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":http4s_blaze_core",
        ":http4s_server"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_blaze_server_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "http4s_core",
    exports = [
        "//3rdparty/jvm/co/fs2:fs2_core",
        "//3rdparty/jvm/co/fs2:fs2_io",
        "//3rdparty/jvm/com/comcast:ip4s_core",
        "//3rdparty/jvm/org/log4s:log4s",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scodec:scodec_bits",
        "//3rdparty/jvm/org/slf4j:slf4j_api",
        "//3rdparty/jvm/org/typelevel:case_insensitive",
        "//3rdparty/jvm/org/typelevel:cats_core",
        "//3rdparty/jvm/org/typelevel:cats_effect_std",
        "//3rdparty/jvm/org/typelevel:cats_parse",
        "//3rdparty/jvm/org/typelevel:literally",
        "//3rdparty/jvm/org/typelevel:log4cats_slf4j",
        "//3rdparty/jvm/org/typelevel:vault",
        ":http4s_crypto"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_core_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "http4s_crypto",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scodec:scodec_bits",
        "//3rdparty/jvm/org/typelevel:cats_core",
        "//3rdparty/jvm/org/typelevel:cats_effect_kernel"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_crypto_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "http4s_dsl",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":http4s_core"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_dsl_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "http4s_prometheus_metrics",
    exports = [
        "//3rdparty/jvm/io/prometheus:simpleclient",
        "//3rdparty/jvm/io/prometheus:simpleclient_common",
        "//3rdparty/jvm/io/prometheus:simpleclient_hotspot",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":http4s_core"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_prometheus_metrics_2_13"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "http4s_server",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":http4s_core"
    ],
    jars = [
        "//external:jar/org/http4s/http4s_server_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


