import extensions.getComponentVersionName
import extensions.getCustomVersionCode
import java.util.Properties

plugins {
    alias(libs.plugins.convention.application)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.firebase)
    alias(libs.plugins.convention.hilt)

    // Non-convention
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.inputStream()
        ?.use { load(it) }
}
val googleClientId: String = localProperties.getProperty("googleClientId").orEmpty()

android {
    namespace = "com.sgale.gaztelubira"

    defaultConfig {
        applicationId = "com.sgale.gaztelubira"
        versionCode = getCustomVersionCode()
        versionName = getComponentVersionName()

        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime)

    /**
     * Project
     */
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:screens"))
}
