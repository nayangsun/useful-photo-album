plugins {
    id("com.android.library")
    kotlin("android")
}

android {
}

dependencies {
    implementation(Libs.Kotlin.coroutine)

    implementation(Libs.inject)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
