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

    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)
    
    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
