load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")
java_library(
    name = "junit_interface",
    exports = [
        "//3rdparty/jvm/junit:junit",
        "//3rdparty/jvm/org/scala_sbt:test_interface",
        "//external:jar/org/scalameta/junit_interface"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "munit",
    exports = [
        "//3rdparty/jvm/junit:junit",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_lang:scala_reflect",
        ":junit_interface"
    ],
    jars = [
        "//external:jar/org/scalameta/munit_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "munit_scalacheck",
    exports = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scalacheck:scalacheck",
        ":munit"
    ],
    jars = [
        "//external:jar/org/scalameta/munit_scalacheck_2_13"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


