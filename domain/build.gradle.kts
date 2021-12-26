plugins {
    id("com.android.library")
    kotlin("android")
}

android {
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data"))

}
