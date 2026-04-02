plugins {
    `kotlin-dsl`
}

group = "com.neil.trantools.buildlogic"

dependencies {
    implementation("com.android.tools.build:gradle:8.13.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:2.0.21")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.57.2")
}
