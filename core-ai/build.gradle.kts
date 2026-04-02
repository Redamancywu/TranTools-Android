plugins {
    id("trantools.android.library")
}

android {
    namespace = "com.neil.trantools.core.ai"
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-database"))
    implementation(project(":core-resource"))
    implementation(libs.google.mediapipe.tasks.genai)
    testImplementation(libs.junit)
}
