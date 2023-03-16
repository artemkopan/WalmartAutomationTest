plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.walmart.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.coroutines.get()}")
    api("com.google.code.gson:gson:${libs.versions.gson.get()}")
    api("com.squareup.retrofit2:retrofit:${libs.versions.retrofit.get()}")
    api("com.squareup.retrofit2:converter-gson:${libs.versions.retrofit.get()}")
    api("com.squareup.okhttp3:okhttp:${libs.versions.okhttp.get()}")
    api("com.squareup.okhttp3:logging-interceptor:${libs.versions.okhttp.get()}")
}