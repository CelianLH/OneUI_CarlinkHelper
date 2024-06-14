pluginManagement {
    repositories {
        google()
		mavenLocal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.aliyun.com/nexus/content/groups/public/")
        mavenCentral()
    }
}

rootProject.name = "CarlinkHelper"
include(":app")
