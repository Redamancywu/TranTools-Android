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
    testImplementation(libs.junit)
}
