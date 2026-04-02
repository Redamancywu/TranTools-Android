plugins {
    id("trantools.android.library")
}

android {
    namespace = "com.neil.trantools.core.data"
}

dependencies {
    api(project(":core-ai"))
    api(project(":core-database"))
    api(project(":core-model"))
    api(project(":core-resource"))
}
