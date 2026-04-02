plugins {
    id("trantools.android.application")
}

android {
    namespace = "com.neil.trantools"

    defaultConfig {
        applicationId = "com.neil.trantools"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-ui"))
    implementation(project(":feature-chat"))
    implementation(project(":feature-gems"))
    implementation(project(":feature-history"))
    implementation(project(":feature-home"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-translate"))
    implementation(project(":feature-voice"))
    implementation(project(":feature-wiki"))
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.runtime.ktx)
}
