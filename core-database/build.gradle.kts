plugins {
    id("trantools.android.library")
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.neil.trantools.core.database"
}

dependencies {
    implementation(project(":core-model"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.junit)
}
