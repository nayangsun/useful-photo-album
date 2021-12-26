plugins {
    id("com.android.library")
    kotlin("android")
}

android {
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data"))

    implementation(Libs.inject)

    implementation(Libs.AndroidX.Lifecycle.livedata)

    implementation(Libs.Kotlin.coroutine)
    implementation(Libs.timber)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
