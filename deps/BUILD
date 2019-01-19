java_plugin(
    name = "auto_value_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["@auto_value//jar"],
)

java_plugin(
    name = "auto_one_of_plugin",
    processor_class = "com.google.auto.value.processor.AutoOneOfProcessor",
    deps = ["@auto_value//jar"],
)

java_library(
    name = "auto_value",
    exported_plugins = [
        ":auto_one_of_plugin",
        ":auto_value_plugin",
    ],
    neverlink = 1,
    visibility = ["//visibility:public"],
    exports = ["@auto_value//jar"],
)

java_library(
    name = "guava",
    visibility = ["//visibility:public"],
    exports = ["@guava//jar"],
)

java_library(
    name = "jsr305",
    visibility = ["//visibility:public"],
    exports = ["@jsr305//jar"],
)

java_library(
    name = "truth",
    visibility = ["//visibility:public"],
    exports = [
        "@diff_utils//jar",
        "@truth//jar",
    ],
)
