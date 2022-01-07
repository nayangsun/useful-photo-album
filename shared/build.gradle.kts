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
    implementation(Libs.Kotlin.coroutine)
    implementation(Libs.inject)

    api(Libs.Gson.gson)

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.annotation)

    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)

    implementation(Libs.timber)
    
    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
