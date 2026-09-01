plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sgale.gaztelubira.core.domain"
}

dependencies {
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.compose.activity)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization)
}
