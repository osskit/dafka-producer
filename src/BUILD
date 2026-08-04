load("@bazel_skylib//rules:copy_file.bzl", "copy_file")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_binary", "scala_library", "scala_test")
load("@rules_oci//oci:defs.bzl", "oci_image", "oci_tarball")
load("@rules_pkg//pkg:tar.bzl", "pkg_tar")

scala_library(
    name = "lib",
    srcs = glob(["main/scala/**/*.scala"]),
    deps = [
        "@third_party//3rdparty/jvm/ch/qos/logback:logback_classic",
        "@third_party//3rdparty/jvm/ch/qos/logback:logback_core",
        "@third_party//3rdparty/jvm/ch/qos/logback/contrib:logback_jackson",
        "@third_party//3rdparty/jvm/ch/qos/logback/contrib:logback_json_classic",
        "@third_party//3rdparty/jvm/com/banno:kafka4s",
        "@third_party//3rdparty/jvm/com/beachape:enumeratum",
        "@third_party//3rdparty/jvm/com/fasterxml/jackson/core:jackson_core",
        "@third_party//3rdparty/jvm/com/fasterxml/jackson/core:jackson_databind",
        "@third_party//3rdparty/jvm/eu/timepit:refined",
        "@third_party//3rdparty/jvm/io/circe:circe_core",
        "@third_party//3rdparty/jvm/io/circe:circe_generic",
        "@third_party//3rdparty/jvm/is/cir:ciris",
        "@third_party//3rdparty/jvm/is/cir:ciris_enumeratum",
        "@third_party//3rdparty/jvm/is/cir:ciris_refined",
        "@third_party//3rdparty/jvm/org/endpoints4s:algebra",
        "@third_party//3rdparty/jvm/org/endpoints4s:http4s_server",
        "@third_party//3rdparty/jvm/org/endpoints4s:json_schema_circe",
        "@third_party//3rdparty/jvm/org/endpoints4s:json_schema_generic",
        "@third_party//3rdparty/jvm/org/http4s:http4s_blaze_server",
        "@third_party//3rdparty/jvm/org/http4s:http4s_dsl",
        "@third_party//3rdparty/jvm/org/http4s:http4s_prometheus_metrics",
        "@third_party//3rdparty/jvm/org/slf4j:slf4j_api",
        "@third_party//3rdparty/jvm/org/typelevel:log4cats_core",
        "@third_party//3rdparty/jvm/org/typelevel:log4cats_slf4j",
    ],
)

scala_binary(
    name = "main",
    srcs = [],
    main_class = "Main",
    resources = ["main/resources/logback.xml"],
    deps = [":lib"],
)

copy_file(
    name = "app_jar",
    src = ":main_deploy.jar",
    out = "dafka-producer.jar",
)

pkg_tar(
    name = "app_layer",
    srcs = [":app_jar"],
    package_dir = "/app",
)

oci_image(
    name = "image",
    base = "@java_base_image",
    entrypoint = [
        "java",
        "-cp",
        "/app/dafka-producer.jar",
        "Main",
    ],
    env = {"JAVA_RUNFILES": "/app"},
    tars = [":app_layer"],
)

oci_tarball(
    name = "image_tarball",
    image = ":image",
    repo_tags = ["bazel/src:image"],
)

# `//src:image.tar` in `bazel-bin/src/` is the contract the release workflow and
# the `pretest` script rely on; oci_tarball writes to `image_tarball/tarball.tar`.
copy_file(
    name = "image_tar",
    src = ":image_tarball",
    out = "image.tar",
)
