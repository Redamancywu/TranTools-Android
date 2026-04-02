plugins {
    id("trantools.android.core-ui")
}

android {
    namespace = "com.neil.trantools.core.ui"
}

dependencies {
    api(project(":core-model"))
}
