plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {

}

dependencies {
    implementation(project(":shared"))
    implementation(project(":domain"))
    implementation(Libs.AndroidX.dataStorePreferences)

    api(Libs.Gson.gson)
    implementation(Libs.Square.okhttp3_logging)
    api(Libs.Square.retrofit)
    api(Libs.Square.converter_gson)


    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.timber)
}

val getUnsplashAccess = project.findProperty("unsplash_access_key")
