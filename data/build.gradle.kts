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

        buildConfigField("String", "REGISTRATION_ENDPOINT_URL", "\"https://events-dev-62d2e.appspot.com/_ah/api/registration/v1/register\"")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":domain"))

    implementation(Libs.AndroidX.dataStorePreferences)

    implementation(Libs.AndroidX.Room.ktx)
    implementation(Libs.AndroidX.Room.runtime)
    kapt(Libs.AndroidX.Room.compiler)

    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)

    api(Libs.Gson.gson)

    api(Libs.Square.retrofit)
    api(Libs.Square.converter_gson)
    implementation(Libs.Square.okhttp3_logging)

    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)

    implementation(Libs.timber)

    implementation(Libs.Firebase.functions)
    implementation(Libs.Firebase.firestore )
    implementation(Libs.Firebase.config)
    implementation(Libs.Firebase.auth)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)

}



