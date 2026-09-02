plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sgale.gaztelubira.core.screens"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.exifinterface)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lottie.compose)

    /**
     * Project
     */
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:preview"))

    /**
     * Multiplatform
     */
    implementation(project(":common:ui"))
}
