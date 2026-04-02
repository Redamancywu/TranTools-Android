plugins {
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

kapt {
    correctErrorTypes = true
}

dependencies {
    add("implementation", "com.google.dagger:hilt-android:2.57.2")
    add("kapt", "com.google.dagger:hilt-compiler:2.57.2")
    add("implementation", "androidx.hilt:hilt-navigation-compose:1.3.0")
}
