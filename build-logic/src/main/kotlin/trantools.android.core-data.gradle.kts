plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    add("implementation", "androidx.core:core-ktx:1.17.0")
    add("implementation", "androidx.datastore:datastore-preferences:1.2.1")
    add("implementation", "androidx.room:room-runtime:2.8.4")
    add("implementation", "androidx.room:room-ktx:2.8.4")
    add("implementation", "com.google.mlkit:translate:17.0.3")
    add("kapt", "androidx.room:room-compiler:2.8.4")
    add("testImplementation", "junit:junit:4.13.2")
}
