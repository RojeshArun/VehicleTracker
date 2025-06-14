plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // If you plan to use Hilt, uncomment these:
    // id("com.google.dagger.hilt.android") version "2.56.2"
    // id("kotlin-kapt") // Or id("com.google.devtools.ksp") version "x.y.z"
}

android {
    namespace = "com.tcs.vehicletrackermaps"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tcs.vehicletrackermaps"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Add your Google API Key here for secure access in BuildConfig
        // Make sure you have Maps_API_KEY="YOUR_ACTUAL_KEY" in your project's local.properties file
        buildConfigField("String", "Maps_API_KEY", "\"${project.findProperty("Maps_API_KEY") ?: "YOUR_FALLBACK_API_KEY_FOR_DEV"}\"")
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
        // ADD THIS LINE:
        buildConfig = true // Enables the BuildConfig feature for custom fields
        compose = true
    }
    composeOptions {
        // The kotlin-compose plugin often handles this automatically.
        // If you encounter "Kotlin compiler extension not found" errors,
        // you might need to explicitly set this, matching your 'composeBom' version's compatibility.
        // For '2024.09.00' BOM, you might need '1.5.15' or later (check Compose compiler releases).
        // kotlinCompilerExtensionVersion = "1.5.15" // Uncomment if explicit setting is needed
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.places)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}