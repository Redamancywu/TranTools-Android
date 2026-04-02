plugins {
    id("trantools.android.library")
}

android {
    namespace = "com.neil.trantools.core.resource"
}

dependencies {
    implementation(project(":core-model"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.google.mlkit.translate)
    testImplementation(libs.junit)
}
