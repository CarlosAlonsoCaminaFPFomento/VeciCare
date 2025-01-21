//Archivo donde se realiza la implementación de dependencias de la aplicación (Module :app)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
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
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas") // Esquemas de Room para validación
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
    implementation(libs.androidx.storage)

    // Jetpack Compose Debugging Tools
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler.v250)
    implementation(libs.androidx.room.ktx)

    //Jetpack Navigation
    implementation(libs.androidx.navigation.compose)
}