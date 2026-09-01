plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.hilt)
}

android {
    namespace = "com.sgale.gaztelubira.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)

    /**
     * Project
     */
    implementation(project(":core:domain"))
}
