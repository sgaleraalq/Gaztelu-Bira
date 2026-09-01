plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sgale.gaztelubira.core.domain"
}

dependencies {
    implementation(libs.kotlinx.serialization)
}
