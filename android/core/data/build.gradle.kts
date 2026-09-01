plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.firebase)
    alias(libs.plugins.convention.hilt)
    alias(libs.plugins.convention.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sgale.gaztelubira.core.data"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.playservices)
    implementation(libs.androidx.googleid)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)

    /**
     * Firebase SDKs that are only useful when called, so they are declared here rather
     * than in the Firebase convention plugin.
     */
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    /**
     * Project
     */
    implementation(project(":core:domain"))
}
