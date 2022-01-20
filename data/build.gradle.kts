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
        buildConfigField("String", "CONFERENCE_DATA_URL", "\"https://firebasestorage.googleapis.com/v0/b/io2019-festivus/o/sessions.json?alt=media&token=019af2ec-9fd1-408e-9b86-891e4f66e674\"")
    }
}

dependencies {
    implementation(project(":shared"))

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
    androidTestImplementation(Libs.Test.espressoCore)

}



