plugins {
    id("trantools.android.feature")
}

android {
    namespace = "com.neil.trantools.feature.home"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-ui"))
}
