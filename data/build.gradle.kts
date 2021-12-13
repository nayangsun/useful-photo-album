plugins {
    id("com.android.library")
    kotlin("android")
}

android {
}

dependencies {
    api(Libs.Gson.gson)
    implementation(Libs.Square.okhttp3_logging)
    api(Libs.Square.retrofit)
    api(Libs.Square.converter_gson)
}
