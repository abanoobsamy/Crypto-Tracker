plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.bob.cryptotracker"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.bob.cryptotracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String", "BASE_URL",
                "\"https://rest.coincap.io/v3/\""
            )
            buildConfigField(
                "String", "TOKEN",
                "\"f29a308aa7390b3f85fb4ea347ec4383a1fa69037db10f1977393ed65cdeeb5c\""
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            /**
             * without \ it make error to handle string
             **/
            buildConfigField(
                "String", "BASE_URL",
                "\"https://rest.coincap.io/v3/\""
            )
            buildConfigField(
                    "String", "TOKEN",
            "\"f29a308aa7390b3f85fb4ea347ec4383a1fa69037db10f1977393ed65cdeeb5c\""
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
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.bundles.koin)

    implementation(libs.bundles.ktor)

    implementation(libs.androidx.adaptive.navigation)

}