plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.indian.calendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.indian.calendar"
        minSdk = 24  // થોડો વધાર્યો છે જેથી નવી લાઇબ્રેરીઓ સારી રીતે ચાલે
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // --- ગૂગલ શીટમાંથી ડેટા લાવવા માટે (OkHttp) ---
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // --- JSON પ્રોસેસિંગ માટે ---
    implementation("org.json:json:20230227")

    // --- ML KIT (જો તમારે ભવિષ્યમાં ભાષાંતર કરવું હોય તો) ---
    implementation("com.google.mlkit:translate:17.0.1")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
