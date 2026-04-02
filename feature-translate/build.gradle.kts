plugins {
    id("trantools.android.feature")
}

android {
    namespace = "com.neil.trantools.feature.translate"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-ui"))
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.google.mlkit.text.recognition)
    implementation(libs.google.mlkit.translate)
}
