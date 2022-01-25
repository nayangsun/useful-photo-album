object Versions {
    const val compileSdk = 31
    const val buildTools = "30.0.1"

    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val googleService = "4.3.3"

    const val COMPILE_SDK = 30
    const val TARGET_SDK = 30
    const val MIN_SDK = 21

    const val ANDROID_GRADLE_PLUGIN = "7.0.0-beta05"
    const val BENCHMARK = "1.0.0"
    const val COMPOSE = "1.0.0-beta04"
    const val FIREBASE_CRASHLYTICS = "2.3.0"
    const val GOOGLE_SERVICES = "4.3.3"
    const val KOTLIN = "1.4.32"
    const val NAVIGATION = "2.3.5"
    const val HILT_AGP = "2.36"

    // TODO: Remove this once the version for
    //  "org.threeten:threetenbp:${Versions.threetenbp}:no-tzdb" using java-platform in the
    //  depconstraints/build.gradle.kts is defined
    const val THREETENBP = "1.3.6"

    private val versionCodeBase = 70150 // XYYZZM; M = Module (tv, mobile)
    val versionCodeMobile = versionCodeBase + 3

}


object Libs {

    // ------------------------------------------------- temp ---------------------------------------------
    const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose"
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx"
    const val APPCOMPAT = "androidx.appcompat:appcompat"
    const val APP_STARTUP = "androidx.startup:startup-runtime"
    const val ARCH_TESTING = "androidx.arch.core:core-testing"
    const val ARCORE = "com.google.ar:core"
    const val BENCHMARK = "androidx.benchmark:benchmark-junit4"
    const val BENCHMARK_MACRO = "androidx.benchmark:benchmark-macro-junit4"
    const val BROWSER = "androidx.browser:browser"
    const val CARDVIEW = "androidx.cardview:cardview"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout"
    const val CORE_KTX = "androidx.core:core-ktx"
    const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics"
    const val COMPOSE_ANIMATION = "androidx.compose.animation:animation"
    const val COMPOSE_MATERIAL = "androidx.compose.material:material"
    const val COMPOSE_RUNTIME = "androidx.compose.runtime:runtime"
    const val COMPOSE_TEST = "androidx.compose.ui:ui-test-junit4"
    const val COMPOSE_THEME_ADAPTER = "com.google.android.material:compose-theme-adapter"
    const val COMPOSE_TOOLING = "androidx.compose.ui:ui-tooling"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test"
    const val DATA_STORE_PREFERENCES = "androidx.datastore:datastore-preferences"
    const val DRAWER_LAYOUT = "androidx.drawerlayout:drawerlayout"
    const val ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core"
    const val EXT_JUNIT = "androidx.test.ext:junit"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx"
    const val FIREBASE_CONFIG = "com.google.firebase:firebase-config-ktx"
    const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
    const val FIREBASE_FUNCTIONS = "com.google.firebase:firebase-functions-ktx"
    const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging"
    const val FIREBASE_UI_AUTH = "com.firebaseui:firebase-ui-auth"
    const val FLEXBOX = "com.google.android:flexbox"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx"
    const val FRAGMENT_TEST = "androidx.fragment:fragment-testing"
    const val GLIDE = "com.github.bumptech.glide:glide"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler"
    const val GOOGLE_MAP_UTILS_KTX = "com.google.maps.android:maps-utils-ktx"
    const val GOOGLE_PLAY_SERVICES_MAPS_KTX = "com.google.maps.android:maps-ktx"
    const val GOOGLE_PLAY_SERVICES_VISION = "com.google.android.gms:play-services-vision"
    const val GSON = "com.google.code.gson:gson"
    const val HAMCREST = "org.hamcrest:hamcrest-library"
    const val HILT_ANDROID = "com.google.dagger:hilt-android"
    const val HILT_COMPILER = "com.google.dagger:hilt-android-compiler"
    const val HILT_TESTING = "com.google.dagger:hilt-android-testing"
    const val INK_PAGE_INDICATOR = "com.pacioianu.david:ink-page-indicator"
    const val JUNIT = "junit:junit"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk7"
    const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler"
    const val LIFECYCLE_LIVE_DATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx"
    const val LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx"
    const val LIFECYCLE_VIEW_MODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx"
    const val LOTTIE = "com.airbnb.android:lottie"
    const val MATERIAL = "com.google.android.material:material"
    const val MDC_COMPOSE_THEME_ADAPTER = "com.google.android.material:compose-theme-adapter"
    const val MOCKITO_CORE = "org.mockito:mockito-core"
    const val MOCKITO_KOTLIN = "com.nhaarman:mockito-kotlin"
    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx"
    const val OKHTTP = "com.squareup.okhttp3:okhttp"
    const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor"
    const val OKIO = "com.squareup.okio:okio"
    const val ROOM_COMPILER = "androidx.room:room-compiler"
    const val ROOM_KTX = "androidx.room:room-ktx"
    const val ROOM_RUNTIME = "androidx.room:room-runtime"
    const val RULES = "androidx.test:rules"
    const val RUNNER = "androidx.test:runner"
    const val SLIDING_PANE_LAYOUT = "androidx.slidingpanelayout:slidingpanelayout"
    const val THREETENABP = "com.jakewharton.threetenabp:threetenabp"
    const val THREETENBP = "org.threeten:threetenbp"
    const val TIMBER = "com.jakewharton.timber:timber"
    const val VIEWMODEL_COMPOSE = "androidx.lifecycle:lifecycle-viewmodel-compose"
    const val VIEWPAGER2 = "androidx.viewpager2:viewpager2"
    const val UI_AUTOMATOR = "androidx.test.uiautomator:uiautomator"
    // ------------------------------------------------- temp ---------------------------------------------


    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.3"

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.7.0"
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
        const val slidingPaneLayout = "androidx.slidingpanelayout:slidingpanelayout:1.2.0-alpha01"
        const val cardView = "androidx.cardview:cardview:1.0.0"
        const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

        object Lifecycle {
            private const val lifecycleVersion = "2.4.0"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
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
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.4.0"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:3.2.0"
    }

    object Firebase {
        // 애널리틱스
        const val analytics = "com.google.firebase:firebase-analytics-ktx:17.4.0"
        // 인증
        const val auth = "com.google.firebase:firebase-auth-ktx:19.3.1"
        // 원격 구성
        const val config = "com.google.firebase:firebase-config-ktx:19.1.4"
        // Cloud Firestore
        const val firestore = "com.google.firebase:firebase-firestore-ktx:21.4.3"
        // Firebase용 Cloud Functions 클라이언트 SDK
        const val functions = "com.google.firebase:firebase-functions-ktx:19.0.2"
        // 클라우드 메시징
        const val messaging = "com.google.firebase:firebase-messaging:20.1.6"
        const val uiAuth = "com.firebaseui:firebase-ui-auth:4.0.0"
        const val crashlytics = "com.google.firebase:firebase-crashlytics:17.2.2"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val inject = "javax.inject:javax.inject:1"

    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.0.5"
    // TODO: Remove this once the version for
    //  "org.threeten:threetenbp:${Versions.threetenbp}:no-tzdb" using java-platform in the
    //  depconstraints/build.gradle.kts is defined
    const val threetenbp = "org.threeten:threetenbp:1.3.6"
    const val lottie = "com.airbnb.android:lottie:3.0.0"

    const val flexbox = "com.google.android:flexbox:1.1.0"

    const val googleService = "com.google.gms:google-services:4.3.3"
    const val googleMapUtils = "com.google.maps.android:maps-utils-ktx:3.0.0"
    const val googlePlayServicesMaps = "com.google.maps.android:maps-ktx:3.0.0"
    const val googlePlayServicesVision = "com.google.android.gms:play-services-vision:17.0.2"
}