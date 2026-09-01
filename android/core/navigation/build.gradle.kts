plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sgale.gaztelubira.core.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.nav3.runtime)
    implementation(libs.nav3.ui)

    /**
     * Project
     */
    implementation(project(":core:domain"))
}
