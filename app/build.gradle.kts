plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
}

android {
    compileSdk = Versions.compileSdk
    defaultConfig {
        applicationId = "com.example.useful_photo_album"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    kapt {
        correctErrorTypes = true
    }

    // Required for AR because it includes a library built with Java 8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // To avoid the compile error: "Cannot inline bytecode built with JVM target 1.8
    // into bytecode that is being built with JVM target 1.6"
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":shared"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)
    implementation(Libs.AndroidX.startup)

    implementation(Libs.Dagger.hiltAndroid)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.Kotlin.coroutine)

    implementation(Libs.timber)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.junitExt)
    androidTestImplementation(Libs.Test.espresso)
}
