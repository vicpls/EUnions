import java.io.FileInputStream
import java.util.Properties

plugins {
    id ("com.android.application")
    id ("com.google.gms.google-services")
    id ("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("dagger.hilt.android.plugin")
}


android {
    compileSdk = 34
    namespace = "netdesigntool.com.eunions"

    defaultConfig {
        applicationId = "netdesigntool.com.eunions"
        minSdk = 21
        targetSdk = 34
        versionCode = 14
        versionName ="1.7"
        //testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "netdesigntool.com.eunions.CustomTestRunner"
        multiDexEnabled = true
//        resourceConfigurations += setOf("en", "ru")
    }

    signingConfigs {
        create("release") {
            val keyStoreFile = rootProject.file("keystore_add.properties")
            if ( ! keyStoreFile.exists())
                println("No keystore file found for release build variant.")
            else{
                val properties = Properties()
                properties.load(FileInputStream(keyStoreFile))
                storeFile = file(properties["storeFile"] as String)
                storePassword = properties["storePassword"] as String
                keyAlias = properties["keyAlias"] as String
                keyPassword = properties["keyPassword"] as String
            }
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            //isDebuggable = true
            //isShrinkResources = true  // теряет строки
            proguardFiles (getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += setOf("META-INF/DEPENDENCIES")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}


dependencies {

    implementation (project(path = ":data"))

    implementation (fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation (libs.androidx.appcompat)
    implementation (libs.androidx.constraintlayout)
    //
    implementation (libs.androidx.recyclerview)
    implementation (libs.material)
    implementation (libs.flexbox)

    implementation (libs.androidx.annotation.experimental)

    implementation (libs.bundles.lifecycle)

    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment.ktx)


    //              Kotlin coroutines
    val coroutinesVer = "1.7.3"
    implementation (libs.kotlinx.coroutines.android)


    //              RxJava
    implementation (libs.rxjava2)

    //              AboutActivity
    implementation (libs.aboutActivity)

    //              MPAndroid charts
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //   =============== Jetpack Compose ====================
    val composeVer = "1.6.1"
    implementation (libs.bundles.compose)
    // Animations
    //implementation 'androidx.compose.animation:animation:1.0.5'

    // Integration with ViewModels
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.accompanist.appcompat.theme)



    //          Paging
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.compose)

    //          Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    //          Room
    implementation (libs.bundles.room)
    kapt (libs.androidx.room.compiler)


    //   Test
    testImplementation (libs.junit)
    testImplementation (libs.core.testing)
    testImplementation (libs.androidx.core)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.inline)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.robolectric)
    testImplementation (libs.awaitility)
    testImplementation (libs.kotlinx.coroutines.test)


    
    //    AndroidTest
    androidTestImplementation (project(path= ":app"))

    androidTestImplementation (libs.androidx.runner)
    androidTestImplementation (libs.androidx.rules)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.mockito.core)
    androidTestImplementation (libs.dexmaker.mockito.inline)
    androidTestImplementation (libs.androidx.espresso.intents)
    androidTestImplementation (libs.androidx.espresso.core)
    androidTestImplementation (libs.awaitility)
    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Needed for createAndroidComposeRule, but not createComposeRule:
    debugImplementation(libs.androidx.ui.test.manifest)

    debugImplementation (libs.androidx.fragment.testing)

    // *** Hilt ***
    // For instrumentation tests
    androidTestImplementation  (libs.hilt.android.testing)
    kaptAndroidTest (libs.hilt.android.kapt.test)
    androidTestAnnotationProcessor (libs.hilt.compiler)

    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.compose.support)
    androidTestUtil(libs.androidx.orchestrator)

}

//  For Hilt with Kotlin
kapt {
    correctErrorTypes = true
}