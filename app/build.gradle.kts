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
        versionCode = 12
        versionName ="1.5"
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
            isShrinkResources = true
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
        kotlinCompilerExtensionVersion = "1.5.4"
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
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    //
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    implementation ("androidx.annotation:annotation-experimental:1.4.0")

    val lifeCycleVer = "2.7.0"
    //noinspection KtxExtensionAvailable
    implementation ("androidx.lifecycle:lifecycle-viewmodel:$lifeCycleVer")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVer")
    implementation ("androidx.lifecycle:lifecycle-common-java8:$lifeCycleVer")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVer")

    implementation ("androidx.activity:activity-ktx:1.8.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")


    //              Kotlin coroutines
    val coroutinesVer = "1.7.3"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVer")


    //              RxJava
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    //              AboutActivity
    implementation ("com.github.biagiopietro:AboutActivity:1.4")

    //              MPAndroid charts
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //   =============== Jetpack Compose ====================
    val composeVer = "1.6.1"
    implementation ("androidx.compose.ui:ui:$composeVer")
    implementation ("androidx.compose.ui:ui-tooling-preview:$composeVer")
    implementation ("androidx.compose.compiler:compiler:1.5.9")
    // Compose Material Design
    implementation ("androidx.compose.material:material:$composeVer")
    // Integration with activities
    implementation ("androidx.activity:activity-compose:1.8.2")
    // Animations
    //implementation 'androidx.compose.animation:animation:1.0.5'
    // Tooling support (Previews, etc.)
    implementation ("androidx.compose.ui:ui-tooling:$composeVer")

    // Integration with ViewModels
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation ("com.google.accompanist:accompanist-appcompat-theme:0.16.0")



    //          Paging
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation ("androidx.paging:paging-compose:3.3.0-alpha02")

    //          Hilt
    val hiltVer = "2.48.1"
    implementation ("com.google.dagger:hilt-android:$hiltVer")
    kapt ("com.google.dagger:hilt-compiler:$hiltVer")

    //          Room
    val roomVer = "2.6.1"
    implementation ("androidx.room:room-runtime:$roomVer")
    kapt ("androidx.room:room-compiler:$roomVer")
    implementation ("androidx.room:room-ktx:$roomVer")
    implementation ("androidx.room:room-paging:$roomVer")


    //   Test
    val mockitoCoreVer = "5.7.0"
    val mockitoInlVer = "4.10.0"
    val mockitoKotlinVer = "4.1.0"
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("android.arch.core:core-testing:1.1.1")
    testImplementation ("androidx.test:core:1.5.0")
    testImplementation ("org.mockito:mockito-core:$mockitoCoreVer")
    testImplementation ("org.mockito:mockito-inline:$mockitoInlVer")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVer")
    testImplementation ("org.robolectric:robolectric:4.9")
    testImplementation ("org.awaitility:awaitility:4.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVer")


    
    //    AndroidTest
    androidTestImplementation (project(path= ":app"))

    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("org.mockito:mockito-core:$mockitoCoreVer")
    androidTestImplementation ("com.linkedin.dexmaker:dexmaker-mockito-inline:2.28.3")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("org.awaitility:awaitility:4.2.0")
    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVer")
    // Needed for createAndroidComposeRule, but not createComposeRule:
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVer")

    debugImplementation ("androidx.fragment:fragment-testing:1.7.0-alpha09")

    // *** Hilt ***
    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:$hiltVer")
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:$hiltVer")
    androidTestAnnotationProcessor ("com.google.dagger:hilt-compiler:$hiltVer")

    val kaspressoVer = "1.5.3"
    androidTestImplementation("com.kaspersky.android-components:kaspresso:$kaspressoVer")
    androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:$kaspressoVer")
    androidTestUtil("androidx.test:orchestrator:1.4.2")

}

//  For Hilt with Kotlin
kapt {
    correctErrorTypes = true
}