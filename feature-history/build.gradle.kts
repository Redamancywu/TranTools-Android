plugins {
    id("trantools.android.feature")
}

android {
    namespace = "com.neil.trantools.feature.history"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-ui"))
}
