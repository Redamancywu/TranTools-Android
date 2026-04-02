plugins {
    id("trantools.android.library.compose")
    id("trantools.android.hilt")
}

dependencies {
    add("implementation", "androidx.core:core-ktx:1.17.0")
    add("implementation", "androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
}
