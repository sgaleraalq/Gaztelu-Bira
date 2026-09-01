plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
}

android {
    namespace = "com.sgale.gaztelubira.core.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    /**
     * Project
     */
    implementation(project(":core:domain"))
}
