plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
    implementation(project(":domain"))

    implementation(Libs.AndroidX.dataStorePreferences)

    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)

    api(Libs.Gson.gson)

    api(Libs.Square.retrofit)
    api(Libs.Square.converter_gson)
    implementation(Libs.Square.okhttp3_logging)


    implementation(Libs.timber)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)

}

