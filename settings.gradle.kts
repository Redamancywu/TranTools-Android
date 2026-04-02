pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TranTools"
include(":app")
include(":core-ai")
include(":core-data")
include(":core-database")
include(":core-model")
include(":core-resource")
include(":core-ui")
include(":feature-chat")
include(":feature-gems")
include(":feature-history")
include(":feature-home")
include(":feature-settings")
include(":feature-translate")
include(":feature-voice")
include(":feature-wiki")
