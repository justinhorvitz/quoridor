java_plugin(
    name = "auto_value_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["@auto_value//jar"],
)

java_library(
    name = "auto_value",
    exported_plugins = [":auto_value_plugin"],
    neverlink = 1,
    exports = ["@auto_value//jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "guava",
    exports = ["@guava//jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "truth",
    exports = [
        "@truth//jar",
        "@diff_utils//jar",
    ],
    visibility = ["//visibility:public"],
)
