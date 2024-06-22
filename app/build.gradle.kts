plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.deggerPlugin)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.app.geradordesenhaaleatorio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.geradordesenhaaleatorio"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }

    buildFeatures{
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    implementation(platform(libs.firebaseBoom))
    implementation(libs.firebaseAuth)
    implementation(libs.firebaseAuthKts)
    implementation(libs.firebaseDatabase)
    implementation(libs.firebaseAnalytics)
    implementation(libs.firebaseFirestore)


    implementation(libs.deggerLib)
    implementation(libs.deggerCompose)
    implementation(libs.deggerComposeNav)
    kapt(libs.kaptLib)
    kapt(libs.kaptLib1)


}