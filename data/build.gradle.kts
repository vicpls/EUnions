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
                isMinifyEnabled = false
//                isMinifyEnabled = true    // не сохраняются сериализованные классы
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
    buildFeatures {
        buildConfig = true
    }
}


dependencies {

    implementation (libs.androidx.core.ktx)

    //          Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    //          Retrofit
    implementation (libs.bundles.retrofit)

    //          RxJava
    implementation (libs.rxjava2)

    //          Room
    implementation (libs.bundles.room)
    kapt (libs.androidx.room.compiler)

    //          Firebase
    // Import the BoM for the Firebase platform
    implementation( platform ("com.google.firebase:firebase-bom:31.1.1"))
    implementation ("com.google.firebase:firebase-database-ktx")
    //implementation 'com.google.firebase:firebase-appcheck-safetynet'  //deprecated
    // App Check library - replacement for safetynet
    //implementation 'com.google.firebase:firebase-appcheck-playintegrity'
    //implementation 'com.google.firebase:firebase-appcheck-debug:16.1.0'

    testImplementation (libs.junit)
    testImplementation (libs.mockito.inline)
    testImplementation (libs.mockito.kotlin)
}