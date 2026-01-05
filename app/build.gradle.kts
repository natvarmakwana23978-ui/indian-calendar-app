plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.kapt' // આ લાઈન ડેટાબેઝ માટે ખૂબ જરૂરી છે
}

android {
    namespace 'com.indian.calendar'
    compileSdk 34

    defaultConfig {
        applicationId "com.indian.calendar"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // ૧. Google ML Kit (ભાષાંતર માટે)
    implementation 'com.google.mlkit:translate:17.0.1'

    // ૨. OkHttp (ગૂગલ શીટ ડેટા લોડ કરવા માટે)
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'

    // ૩. Room Database (ડિજિટલ ડટ્ટા અને પર્સનલ નોટ્સ માટે)
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // કોર્યુટિન્સ (બેકગ્રાઉન્ડ ટાસ્ક માટે)
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}

