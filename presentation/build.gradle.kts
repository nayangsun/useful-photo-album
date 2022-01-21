plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        buildConfigField(
            "com.google.android.gms.maps.model.LatLng",
            "MAP_VIEWPORT_BOUND_NE",
            "new com.google.android.gms.maps.model.LatLng(${project.properties["map_viewport_bound_ne"]})"
        )
        buildConfigField(
            "com.google.android.gms.maps.model.LatLng",
            "MAP_VIEWPORT_BOUND_SW",
            "new com.google.android.gms.maps.model.LatLng(${project.properties["map_viewport_bound_sw"]})"
        )

        buildConfigField("float", "MAP_CAMERA_FOCUS_ZOOM", project.properties["map_camera_focus_zoom"] as String)

        resValue("dimen", "map_camera_bearing", project.properties["map_default_camera_bearing"] as String)
        resValue("dimen", "map_camera_target_lat", project.properties["map_default_camera_target_lat"] as String)
        resValue("dimen", "map_camera_target_lng", project.properties["map_default_camera_target_lng"] as String)
        resValue("dimen", "map_camera_tilt", project.properties["map_default_camera_tilt"] as String)
        resValue("dimen", "map_camera_zoom", project.properties["map_default_camera_zoom"] as String)
        resValue("dimen", "map_viewport_min_zoom", project.properties["map_viewport_min_zoom"] as String)
        resValue("dimen", "map_viewport_max_zoom", project.properties["map_viewport_max_zoom"] as String)

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
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.slidingPaneLayout)
    implementation(Libs.AndroidX.browser)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    kapt(Libs.Glide.compiler)
    implementation(Libs.Glide.glide)

    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)
    implementation(Libs.timber)
    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)
    implementation(Libs.lottie)

    implementation(Libs.Firebase.firestore)

    implementation(Libs.flexbox)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espressoCore)
}

apply(plugin = "com.google.gms.google-services")
