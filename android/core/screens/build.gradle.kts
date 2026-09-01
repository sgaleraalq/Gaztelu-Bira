plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
}

android {
    namespace = "com.sgale.gaztelubira.core.screens"
}

dependencies {
    implementation(project(":core:designsystem"))
}
