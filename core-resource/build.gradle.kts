plugins {
    id("trantools.android.library")
}

android {
    namespace = "com.neil.trantools.core.resource"
}

dependencies {
    implementation(project(":core-database"))
    implementation(project(":core-model"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.google.play.billing.ktx)
    implementation(libs.google.mlkit.translate)
    testImplementation(libs.junit)
}
