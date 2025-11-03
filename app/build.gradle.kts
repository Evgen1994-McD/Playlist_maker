plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
    alias(libs.plugins.kotlin.compose)

//    id("com.google.devtools.ksp") version "2.2.0-2.0.2"  // для KSP

}

android {
    namespace = "com.example.playlistmaker"
    compileSdk = 36

    android {
        buildFeatures {
            viewBinding = true // Включаю вьюбайдинг
            compose = true
        }
    }

    defaultConfig {
        applicationId = "com.example.playlistmaker"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_1_9
        targetCompatibility = JavaVersion.VERSION_1_9
    }
    kotlinOptions {
        jvmTarget = "9"
    }
}

dependencies {


    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)


// Или для Coroutine Flows
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Koin
    implementation(libs.koin)


    // ViewModel
    implementation(libs.view.model)
    // liveData
    implementation(libs.live.data)
    implementation(libs.fragment)
implementation(libs.glide)
annotationProcessor(libs.annotation.processor)
implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation(libs.logger.interceptor)
implementation(libs.recycler.view)


    /***
     * Ниже все зависимости на Compose
     */

    // ViewModel and Compose integration
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")


    androidTestImplementation(platform(libs.androidx.compose.bom))


    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)




    implementation("androidx.compose.runtime:runtime:1.5.9")

    // Compose UI
    implementation("androidx.compose.ui:ui:1.5.9")
// Material 3
    implementation("androidx.compose.material3:material3:1.2.0")
// Интеграция Compose с View-системами
    implementation("androidx.compose.ui:ui-viewbinding:1.5.9")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.9")


    implementation(libs.coil.compose)






}