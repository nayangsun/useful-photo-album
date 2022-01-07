object Versions {
    const val compileSdk = 31
    const val buildTools = "30.0.1"

    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0.0"
}


object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.3"

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        object Activity {
            private const val version = "1.3.1"
            const val activity = "androidx.activity:activity-ktx:$version"
            const val compose = "androidx.activity:activity-compose:$version"
        }
        const val fragment = "androidx.fragment:fragment-ktx:1.3.5"
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val browser = "androidx.browser:browser:1.3.0"
        const val viewpager2 ="androidx.viewpager2:viewpager2:1.0.0"
        const val paging3 = "androidx.paging:paging-runtime:3.1.0"
        const val work = "androidx.work:work-runtime-ktx:2.7.1"
        const val dataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0-beta01"
        const val startup = "androidx.startup:startup-runtime:1.1.0-beta01"
        const val annotation = "androidx.annotation:annotation:1.3.0"

        object Lifecycle {
            private const val lifecycleVersion = "2.3.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        }

        object Navigation {
            private const val version = "2.3.5"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val args = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Room {
            private const val version = "2.2.5"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }
    }

    object Kotlin {
        const val version = "1.5.21"
        const val coroutineVersion = "1.5.1"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.2.1"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"

        object Test {
            const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        }
    }

    object Glide {
        private const val version = "4.10.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Dagger {
        private const val daggerVersion = "2.38.1"
        const val hiltAndroid = "com.google.dagger:hilt-android:$daggerVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$daggerVersion"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerVersion"
    }

    object Square {
        private const val retrofitVersion = "2.9.0"
        const val okhttp3_logging = "com.squareup.okhttp3:logging-interceptor:4.9.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val converter_gson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        const val serialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Gson {
        const val gson = "com.google.code.gson:gson:2.8.2"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val junitExt = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:3.2.0"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val inject = "javax.inject:javax.inject:1"

    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.0.5"
    // TODO: Remove this once the version for
    //  "org.threeten:threetenbp:${Versions.threetenbp}:no-tzdb" using java-platform in the
    //  depconstraints/build.gradle.kts is defined
    const val threetenbp = "org.threeten:threetenbp:1.3.6"
    const val lottie = "com.airbnb.android:lottie:3.0.0"
}