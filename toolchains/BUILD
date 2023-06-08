load("@io_bazel_rules_scala//scala:scala.bzl", "setup_scala_toolchain")
load("@io_bazel_rules_scala_config//:config.bzl", "SCALA_MAJOR_VERSION")

_SCALA_COMPILE_CLASSPATH_DEPS = [
    "@io_bazel_rules_scala_scala_compiler",
    "@io_bazel_rules_scala_scala_library",
] + (["@io_bazel_rules_scala_scala_reflect"] if SCALA_MAJOR_VERSION.startswith("2") else [
    "@io_bazel_rules_scala_scala_interfaces",
    "@io_bazel_rules_scala_scala_tasty_core",
    "@io_bazel_rules_scala_scala_asm",
    "@io_bazel_rules_scala_scala_library_2",
])

_SCALA_LIBRARY_CLASSPATH_DEPS = [
    "@io_bazel_rules_scala_scala_library",
] + (["@io_bazel_rules_scala_scala_reflect"] if SCALA_MAJOR_VERSION.startswith("2") else [
    "@io_bazel_rules_scala_scala_library_2",
])

_SCALA_MACRO_CLASSPATH_DEPS = [
    "@io_bazel_rules_scala_scala_library",
] + (["@io_bazel_rules_scala_scala_reflect"] if SCALA_MAJOR_VERSION.startswith("2") else [
    "@io_bazel_rules_scala_scala_library_2",
])


setup_scala_toolchain(
    name = "my_scala_toolchain",
    # configure toolchain dependecies
   
    scala_compile_classpath = _SCALA_COMPILE_CLASSPATH_DEPS,
    scala_library_classpath =_SCALA_LIBRARY_CLASSPATH_DEPS,
    scala_macro_classpath =_SCALA_MACRO_CLASSPATH_DEPS,
    
    # example of setting attribute values
    scalacopts = ["-Ywarn-unused", "-deprecation", "-unchecked", "-feature", "-Xlint", "-Xfatal-warnings", "-Xlint:-byname-implicit", "-Xlint"],
    unused_dependency_checker_mode = "off",
    visibility = ["//visibility:public"]
)
