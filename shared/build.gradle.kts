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

        buildConfigField("String", "CONFERENCE_TIMEZONE", project.properties["conference_timezone"] as String)
        buildConfigField("String", "CONFERENCE_DAY1_START", project.properties["conference_day1_start"] as String)
        buildConfigField("String", "CONFERENCE_DAY1_END", project.properties["conference_day1_end"] as String)
        buildConfigField("String", "CONFERENCE_DAY2_START", project.properties["conference_day2_start"] as String)
        buildConfigField("String", "CONFERENCE_DAY2_END", project.properties["conference_day2_end"] as String)
        buildConfigField("String", "CONFERENCE_DAY3_START", project.properties["conference_day3_start"] as String)
        buildConfigField("String", "CONFERENCE_DAY3_END", project.properties["conference_day3_end"] as String)

        buildConfigField("String", "CONFERENCE_DAY1_AFTERHOURS_START", project.properties["conference_day1_afterhours_start"] as String)
        buildConfigField("String", "CONFERENCE_DAY2_CONCERT_START", project.properties["conference_day2_concert_start"] as String)

        buildConfigField(
            "String",
            "BOOTSTRAP_CONF_DATA_FILENAME", project.properties["bootstrap_conference_data_filename"] as String
        )

        buildConfigField(
            "String",
            "CONFERENCE_WIFI_OFFERING_START", project.properties["conference_wifi_offering_start"] as String
        )
    }
}

dependencies {
    implementation(Libs.Kotlin.coroutine)
    implementation(Libs.inject)

    api(Libs.Gson.gson)

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.annotation)

    implementation(Libs.threetenabp)
    implementation(Libs.threetenbp)

    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.timber)
    
    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espressoCore)
}
