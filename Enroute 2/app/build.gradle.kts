plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.enroute"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.enroute"
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
        mlModelBinding = true
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
//  implementation(libs.litert.support.api)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.litert)
    implementation(libs.androidx.espresso.core)
    implementation(libs.litert.support.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // dependencies for firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-ml-modeldownloader")
    implementation("org.tensorflow:tensorflow-lite:2.4.0")
    //dependencies for camera
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("com.squareup.okio:okio:2.3.0")
    // dependencies for depth perception
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // dependency resolution

//    implementation("com.google.ai.edge.litert:litert-api:1.0.1") {
//        exclude("org.tensorflow","tensorflow-lite-support")
//    }

//    configurations.all {
//        resolutionStrategy {
//            force("org.tensorflow:tensorflow-lite:2.3.0")
//        }
//        exclude("org.tensorflow","tensorflow-lite-support")
//        exclude("org.tensorflow", "tensorflow-lite")
//        exclude("org.tensorflow", "tensorflow-lite-runtime")
//    }
//    implementation("org.tensorflow:tensorflow-lite-support:0.1.0-rc1")
//    implementation("com.google.mlkit:object-detection:17.0.2")

//    implementation("org.tensorflow:tensorflow-lite:+")
////    implementation("org.tensorflow:tensorflow-lite-gpu:+")
//    implementation("org.tensorflow:tensorflow-lite-support:+")
}

