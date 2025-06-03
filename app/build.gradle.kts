import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
}

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("keystore_details/keystore.properties")


if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} else {
    println("keystore.properties file not found. Using environment variables.")

    keystoreProperties["storeFile"] = System.getenv("ANDROID_KEYSTORE_PATH") ?: ""
    keystoreProperties["storePassword"] = System.getenv("ANDROID_KEYSTORE_PASSWORD") ?: ""
    keystoreProperties["keyAlias"] = System.getenv("ANDROID_KEY_ALIAS") ?: ""
    keystoreProperties["keyPassword"] = System.getenv("ANDROID_KEY_PASSWORD") ?: ""
}
android {
    namespace = "com.shreyash.dotrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shreyash.dotrack"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.09"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            val storeFilePath = keystoreProperties["storeFile"] as String
            if (storeFilePath.isNotEmpty()) {
                storeFile = file(storeFilePath)
            }
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    implementation(libs.compose.hilt.navigation)

    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.1")
    implementation ("com.google.dagger:hilt-android:2.51") // or latest
    kapt ("com.google.dagger:hilt-compiler:2.51")
    implementation("androidx.hilt:hilt-work:1.1.0") // Hilt + WorkManager integration
    kapt("androidx.hilt:hilt-compiler:1.1.0")        // Hilt code generation

    // Color Picker
    implementation(libs.colorpicker.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)


}