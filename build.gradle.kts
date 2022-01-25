/*
* Copyright 2020 Google LLC
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

// Top-level build file where you can add configuration options common to all
// sub-projects/modules.
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
        classpath("androidx.benchmark:benchmark-gradle-plugin:${Versions.BENCHMARK}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_AGP}")
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.27.1"
}


allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {

    // spotless를 적용하여놓았으나, 해당 프로젝트는 develop용이기 때문에 lint 검사에 적합하진 않다고 생각한다. 나중에 적용해 볼수 있기를..
    apply(plugin = "com.diffplug.gradle.spotless")
    val ktlintVer = "0.40.0"
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(ktlintVer).userData(
                mapOf("max_line_length" to "100", "disabled_rules" to "import-ordering")
            )
            licenseHeaderFile(project.rootProject.file("copyright.kt"))
        }
        kotlinGradle {
            // same as kotlin, but for .gradle.kts files (defaults to '*.gradle.kts')
            target("**/*.gradle.kts")
            ktlint(ktlintVer)
            licenseHeaderFile(project.rootProject.file("copyright.kt"), "(plugins |import |include)")
        }
    }

    // `spotlessCheck` runs when a build includes `check`, notably during presubmit. In these cases
    // we prefer `spotlessCheck` run as early as possible since it fails in seconds. This prevents a
    // build from running for several minutes doing other intensive tasks (resource processing, code
    // generation, compilation, etc) only to fail on a formatting nit.
    // Using `mustRunAfter` avoids creating a task dependency. The order is enforced only if
    // `spotlessCheck` is already scheduled to run, so we can still build and launch from the IDE
    // while the code is "dirty".
//    tasks.whenTaskAdded {
//        if (name == "preBuild") {
//            mustRunAfter("spotlessCheck")
//        }
//    }

    // TODO: Remove when the Coroutine and Flow APIs leave experimental/internal/preview.
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}

