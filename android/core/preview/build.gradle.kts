plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
}

android {
    namespace = "com.sgale.gaztelubira.core.preview"
}

dependencies {
    /**
     * Project
     */
    implementation(project(":core:domain"))
}
