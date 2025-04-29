plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        removeIf { it.name == "projLibs" }
        create("projLibs") {
            from(files("./gradle/libs.versions.toml"))
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "IP4ListSecondAdvent"

include("tools")
include("generator")
include("analyzer")
