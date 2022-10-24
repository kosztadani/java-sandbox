plugins {
    `kotlin-dsl`
}

dependencies {
    val shadowPluginVersion = "7.1.2"
    implementation(group = "gradle.plugin.com.github.johnrengelman", name = "shadow", version = shadowPluginVersion)
}

repositories {
    gradlePluginPortal()
}
