plugins {
    id("trantools.android.feature")
}

android {
    namespace = "com.neil.trantools.feature.voice"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-ui"))
    implementation(libs.google.mlkit.translate)
}
