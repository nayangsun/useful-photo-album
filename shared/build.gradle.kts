plugins {
    id("com.android.library")
    kotlin("android")
}

android {
}

dependencies {
    implementation(Libs.Kotlin.coroutine)
    implementation(Libs.inject)

    api(Libs.Gson.gson)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
