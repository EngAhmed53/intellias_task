plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.videocatalog"
    compileSdk = 37
    // NDK r30 LTS (https://developer.android.com/ndk/downloads). AGP downloads it on first build.
    ndkVersion = "30.0.16248370"

    defaultConfig {
        applicationId = "com.example.videocatalog"
        minSdk = 24          // AMediaExtractor is API 21+, but 24 keeps the NDK/Compose matrix simple
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        aidl = true      // AGP 9 has no global AIDL default; must be enabled per module
        compose = true
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17   // Kotlin jvmTarget follows this (built-in Kotlin)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.1")                       // Kotlin extensions on framework APIs
    implementation("androidx.activity:activity-compose:1.13.0")           // ComponentActivity.setContent {}
    implementation(platform("androidx.compose:compose-bom:2026.09.00"))   // one BOM pins all Compose artifacts
    implementation("androidx.compose.ui:ui")                              // core Compose runtime/UI
    implementation("androidx.compose.material3:material3")                // list items, text, progress indicator
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0") // Dispatchers.IO for the Binder call
}
