plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk = 34
    namespace = "com.hh.data"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // to generate json file with db schema for each db version
        kapt {
            arguments { arg("room.schemaLocation", "$projectDir/schemas") }
        }


        buildTypes {
            release {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            jvmTarget = "17"
        }
    }
}


dependencies {

    implementation ("androidx.core:core-ktx:1.12.0")

    //          Hilt
    val hiltVer = "2.48.1"
    implementation ("com.google.dagger:hilt-android:$hiltVer")
    kapt ("com.google.dagger:hilt-compiler:$hiltVer")

    //          Retrofit
    val retrofit = "com.squareup.retrofit2"
    val retrofitVer = "2.9.0"
    implementation ("$retrofit:retrofit:$retrofitVer")
    implementation ("$retrofit:converter-moshi:$retrofitVer")
    implementation ("$retrofit:adapter-rxjava2:$retrofitVer")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //          RxJava
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    //          Room
    val roomVer = "2.6.0"
    implementation ("androidx.room:room-runtime:$roomVer")
    kapt ("androidx.room:room-compiler:$roomVer")
    implementation ("androidx.room:room-ktx:$roomVer")
    implementation ("androidx.room:room-paging:$roomVer")

    //          Firebase
    // Import the BoM for the Firebase platform
    implementation( platform ("com.google.firebase:firebase-bom:31.1.1"))
    implementation ("com.google.firebase:firebase-database-ktx")
    //implementation 'com.google.firebase:firebase-appcheck-safetynet'  //deprecated
    // App Check library - replacement for safetynet
    //implementation 'com.google.firebase:firebase-appcheck-playintegrity'
    //implementation 'com.google.firebase:firebase-appcheck-debug:16.1.0'

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-inline:4.10.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
}