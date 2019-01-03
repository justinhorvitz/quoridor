package(default_visibility = [":quoridor_packages"])

package_group(
    name = "quoridor_packages",
    packages = [
        "//java/sublimedisruptors/quoridor/...",
        "//javatests/sublimedisruptors/quoridor/...",
    ],
)

java_library(
    name = "quoridor",
    srcs = glob(["*.java"]),
    deps = [
        "//deps:auto_value",
        "//deps:guava",
    ],
)
