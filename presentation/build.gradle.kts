plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    id("kotlin-android")
}

android {
    compileSdk = Versions.compileSdk
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
    buildFeatures {

        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":shared"))

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.material)

    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(project(mapOf("path" to ":data")))
    kapt(Libs.AndroidX.Lifecycle.compiler)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.slidingPaneLayout)
    implementation(Libs.AndroidX.browser)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    kapt(Libs.Glide.compiler)
    implementation(Libs.Glide.glide)

    implementation(Libs.Dagger.hiltAndroid)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    kapt(Libs.Dagger.hiltCompiler)
    implementation(Libs.timber)
    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)
    implementation(Libs.lottie)

    implementation(Libs.flexbox)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
