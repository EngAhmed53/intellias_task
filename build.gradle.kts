// Root build file: declares plugin versions only. The app module applies them.
plugins {
    id("com.android.application") version "9.2.1" apply false
    // AGP 9 compiles Kotlin itself (built-in Kotlin). Only the Compose compiler plugin is needed.
    // Version must match the Kotlin version AGP 9.2 bundles (2.2.10).
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
}
