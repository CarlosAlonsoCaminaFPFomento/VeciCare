//Archivo donde se realiza la implementación de dependencias de la aplicación (Module :app)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.alonsocamina.vecicare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.alonsocamina.vecicare"
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
//Implementación de dependencias
dependencies {
    // Material Design 3
    implementation(libs.material3)

    // Compose Core
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.activity.compose)

    // Google Fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Jetpack Compose Debugging Tools
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}