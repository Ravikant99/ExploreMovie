plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlinx-serialization")
    id("com.google.dagger.hilt.android")
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
//    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "com.ravi.exploremovie"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ravi.exploremovie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //dependency for navigation
    implementation(libs.androidx.navigation)
    implementation (libs.accompanist.pager)
    implementation (libs.androidx.material)
    implementation (libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation) // for HorizontalPager

    // Adding dependencies related to retrofit and Gson converter
    implementation (libs.retrofit) // Retrofit library for making HTTP requests
    implementation (libs.converter.gson) // Gson converter for Retrofit, to serialize and deserialize JSON

   // Coroutine support
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // Glide for image loading
    implementation(libs.glide)
    implementation(libs.compose)

    // Coil for image loading
    implementation(libs.coil.compose)
    // Network
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.activity.compose.v180)
    implementation (libs.ui)
    implementation (libs.material3)
    //youtube player
    implementation (libs.youtubecore)


        // Coroutines
        implementation (libs.kotlinx.coroutines.android.v173)

        // Lifecycle (for ViewModel)
        implementation (libs.androidx.lifecycle.viewmodel.ktx)
        implementation (libs.androidx.lifecycle.runtime.ktx.v262)

        // Retrofit (if you're using it)
        implementation (libs.retrofit)
        implementation (libs.converter.gson)

        // Timber for logging (optional)
        implementation (libs.timber)
    //firebase
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.auth.ktx)
//    implementation(libs.play.services.auth)
}