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
    implementation(project(":data"))
    implementation(Libs.inject)

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.paging3)
    implementation(Libs.AndroidX.Lifecycle.livedata)

    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.Kotlin.coroutine)
    implementation(Libs.timber)

    implementation(Libs.Firebase.functions)
    implementation(Libs.Firebase.firestore )
    implementation(Libs.Firebase.config)
    implementation(Libs.Firebase.auth)

    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
