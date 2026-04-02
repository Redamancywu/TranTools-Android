plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    add("implementation", "androidx.core:core-ktx:1.17.0")
    add("implementation", "androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    add("implementation", "androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    add("implementation", "androidx.activity:activity-compose:1.12.2")
    add("implementation", platform("androidx.compose:compose-bom:2024.09.00"))
    add("implementation", "androidx.compose.ui:ui")
    add("implementation", "androidx.compose.ui:ui-graphics")
    add("implementation", "androidx.compose.ui:ui-tooling-preview")
    add("implementation", "androidx.compose.material3:material3")
    add("implementation", "androidx.compose.material:material-icons-extended")
    add("implementation", "androidx.navigation:navigation-compose:2.9.7")
    add("implementation", "com.google.dagger:hilt-android:2.57.2")
    add("kapt", "com.google.dagger:hilt-compiler:2.57.2")
    add("implementation", "androidx.hilt:hilt-navigation-compose:1.3.0")
    add("testImplementation", "junit:junit:4.13.2")
    add("androidTestImplementation", "androidx.test.ext:junit:1.3.0")
    add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.7.0")
    add("androidTestImplementation", platform("androidx.compose:compose-bom:2024.09.00"))
    add("androidTestImplementation", "androidx.compose.ui:ui-test-junit4")
    add("debugImplementation", "androidx.compose.ui:ui-tooling")
    add("debugImplementation", "androidx.compose.ui:ui-test-manifest")
}
