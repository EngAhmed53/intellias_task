// Root build file: declares plugin versions only. The app module applies them.
plugins {
    id("com.android.application") version "9.2.1" apply false
    // AGP 9 compiles Kotlin itself (built-in Kotlin). Only the Compose compiler plugin is needed.
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.2.21" apply false
}
