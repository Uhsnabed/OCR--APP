val DependencyHandlerScope.implementation: Unit
    get() {}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ocrapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ocrapp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.support.annotations)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core);
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition-devanagari:16.0.1")
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition-chinese:16.0.1")
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition-japanese:16.0.1")
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition-korean:16.0.1")

}