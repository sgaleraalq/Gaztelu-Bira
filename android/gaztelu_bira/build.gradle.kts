import extensions.getComponentVersionName
import extensions.getCustomVersionCode

plugins {
    alias(libs.plugins.convention.application)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.firebase)
    alias(libs.plugins.convention.hilt)

    // Non-convention
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.sgale.gaztelubira"

    defaultConfig {
        applicationId = "com.sgale.gaztelubira"
        versionCode = getCustomVersionCode()
        versionName = getComponentVersionName()
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(libs.nav3.runtime)
    implementation(libs.nav3.ui)
    implementation(libs.androidx.lifecycle.runtime)
}
