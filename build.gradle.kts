// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Android and Kotlin plugins
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false

    // Compose compiler plugin (required in Kotlin 2.0+)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false

    // Optional: if you use Kotlin serialization anywhere
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
