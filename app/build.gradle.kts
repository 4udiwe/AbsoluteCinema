plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id ("com.google.gms.google-services")
}


android {
    namespace = "com.example.absolutecinema"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.absolutecinema"
        minSdk = 26
        targetSdk = 35
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

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":feature:users"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:details"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:search"))

    //Koin
    implementation(libs.koin.android)


    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Retrofit
    implementation(libs.retrofit)
    implementation (libs.converter.gson)

    //Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.common)

    //Okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //SharedPreferences
    implementation(libs.androidx.preference.ktx)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}