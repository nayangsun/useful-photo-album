plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = Versions.compileSdk
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(Libs.inject)

    implementation(Libs.AndroidX.paging3)
    implementation(Libs.AndroidX.Lifecycle.livedata)

    implementation(Libs.Kotlin.coroutine)
    implementation(Libs.timber)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
