package(default_visibility = ["//java/sublimedisruptors/quoridor:quoridor_packages"])

java_library(
    name = "game",
    srcs = glob(["*.java"]),
    deps = [
        "//deps:guava",
        "//java/sublimedisruptors/quoridor",
        "//java/sublimedisruptors/quoridor/board",
        "//java/sublimedisruptors/quoridor/move",
        "//java/sublimedisruptors/quoridor/player",
    ],
)

java_binary(
    name = "PlayGame",
    main_class = "sublimedisruptors.quoridor.game.QuoridorGameMain",
    runtime_deps = [":game"],
)
