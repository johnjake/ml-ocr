import AndroidConfigLib.cameraVersion
import AndroidConfigLib.compatVersion
import AndroidConfigLib.compilerVersion
import AndroidConfigLib.constrainVersion
import AndroidConfigLib.daggerVersion
import AndroidConfigLib.hiltViewModelVersion
import AndroidConfigLib.ktxVersion
import AndroidConfigLib.lifeCycleVersion
import AndroidConfigLib.lottieVersion
import AndroidConfigLib.materialVersion
import AndroidConfigLib.saveStateVersion
import AndroidConfigLib.timberVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 32
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "app.scanner.calc"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions("colorDimen", "typeDimen")
    productFlavors {
        create("builtincamera") {
            dimension = "typeDimen"
            applicationIdSuffix =".camera"
            versionCode = 3
        }
        create("filesystem") {
            dimension = "typeDimen"
            applicationIdSuffix = ".file"
            versionCode = 4
        }

        create("red") {
            dimension = "colorDimen"
            applicationIdSuffix = ".red"
            versionCode = 1
        }
        create("green") {
            dimension = "colorDimen"
            applicationIdSuffix = ".green"
            versionCode = 2
        }
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    val red = "red"
    val green = "green"
    val camera = "builtincamera"
    val file = "filesystem"
    sourceSets {
        this.getByName("$red"){
            this.java.srcDir("src/$red/java")
        }
        this.getByName("$red"){
            this.res.srcDir("src/$red/res")
        }

        this.getByName("$green"){
            this.java.srcDir("src/$green/java")
        }
        this.getByName("$green"){
            this.res.srcDir("src/$green/res")
        }

        this.getByName("$camera"){
            this.java.srcDir("src/$camera/java")
        }
        this.getByName("$camera"){
            this.res.srcDir("src/$camera/res")
        }

        this.getByName("$file"){
            this.java.srcDir("src/$file/java")
        }
        this.getByName("$file"){
            this.res.srcDir("src/$file/res")
        }
    }

    this.buildOutputs.all {
        val variants = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
        val variantName: String = variants.name
        variants.outputFileName = "${android.defaultConfig.applicationId}.apk"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:$ktxVersion")
    implementation("androidx.appcompat:appcompat:$compatVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constrainVersion")

    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-compiler:$compilerVersion")

    /** view model **/
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$hiltViewModelVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$saveStateVersion")

    /** camera class **/
    implementation ("androidx.exifinterface:exifinterface:1.3.3")

    implementation ("androidx.camera:camera-camera2:$cameraVersion")
    implementation ("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation ("androidx.camera:camera-view:1.0.0-alpha27")

    /** google machine learning text recognition **/
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:16.0.0")

    implementation("androidx.fragment:fragment-ktx:1.5.0")
    implementation("com.airbnb.android:lottie:4.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")

    /** navigation **/
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.0")

    /** lifecycle **/
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.0-alpha01")

    /** lifecycle optional **/
    implementation("androidx.lifecycle:lifecycle-service:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-process:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.6.0-alpha01")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha01")

    implementation("com.google.android.gms:play-services-cast-framework:21.0.1")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation("org.beanshell:bsh:2.0b4")

    implementation("com.jakewharton.timber:timber:$timberVersion")

    /** animation **/
    implementation ("com.airbnb.android:lottie:$lottieVersion")

    /** unit test **/
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}