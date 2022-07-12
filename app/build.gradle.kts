import AndroidConfigLib.archCoreVersion
import AndroidConfigLib.cameraVersion
import AndroidConfigLib.compatVersion
import AndroidConfigLib.compilerVersion
import AndroidConfigLib.constrainVersion
import AndroidConfigLib.coroutineVersion
import AndroidConfigLib.daggerVersion
import AndroidConfigLib.expressoCoreVersion
import AndroidConfigLib.hiltViewModelVersion
import AndroidConfigLib.juniVersion
import AndroidConfigLib.ktxVersion
import AndroidConfigLib.lifeCycleVersion
import AndroidConfigLib.lottieVersion
import AndroidConfigLib.materialVersion
import AndroidConfigLib.mokitoCoreVersion
import AndroidConfigLib.saveStateVersion
import AndroidConfigLib.timberVersion
import AndroidConfigLib.mokitoVersion
import AndroidConfigLib.junitExtVersion
import ConfigFlavor.redFlavor
import ConfigFlavor.greenFlavor
import ConfigFlavor.cameraSystem
import ConfigFlavor.fileSystem
import FlavorDimension.colorDimension
import SourceSet.cameraDirJava
import SourceSet.cameraDirRes
import SourceSet.fileDirJava
import SourceSet.fileDirRes
import SourceSet.redDirJava
import SourceSet.redDirRes
import SourceSet.greenDirJava
import SourceSet.greenDirRes
import FlavorDimension.typeDimension
import FlavorVersion.greenCameraSystem
import FlavorVersion.greenFileSystem
import FlavorVersion.redCameraSystem
import FlavorVersion.redFileSystem


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

    flavorDimensions.addAll(listOf(colorDimension, typeDimension))
    productFlavors {
        create(cameraSystem) {
            dimension = typeDimension
            applicationIdSuffix =".camera"
            versionName = redCameraSystem
            versionCode = 3
        }
        create(fileSystem) {
            dimension = typeDimension
            applicationIdSuffix = ".file"
            versionName = greenFileSystem
            versionCode = 4
        }

        create(redFlavor) {
            dimension = colorDimension
            applicationIdSuffix = ".red"
            versionName = redFileSystem
            versionCode = 1
        }
        create(greenFlavor) {
            dimension = colorDimension
            applicationIdSuffix = ".green"
            versionName = greenCameraSystem
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

    sourceSets {
        this.getByName(redFlavor){
            this.java.srcDir(redDirJava)
        }
        this.getByName(redFlavor){
            this.res.srcDir(redDirRes)
        }

        this.getByName(greenFlavor){
            this.java.srcDir(greenDirJava)
        }
        this.getByName(greenFlavor){
            this.res.srcDir(greenDirRes)
        }

        this.getByName(cameraSystem){
            this.java.srcDir(cameraDirJava)
        }
        this.getByName(cameraSystem){
            this.res.srcDir(cameraDirRes)
        }

        this.getByName(fileSystem){
            this.java.srcDir(fileDirJava)
        }
        this.getByName(fileSystem){
            this.res.srcDir(fileDirRes)
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = FlavorVersion.appNameApk(variant.versionName)
            }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))

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

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha01")

    implementation("com.google.android.gms:play-services-cast-framework:21.0.1")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation("org.beanshell:bsh:2.0b4")

    implementation("com.jakewharton.timber:timber:$timberVersion")

    /** animation **/
    implementation ("com.airbnb.android:lottie:$lottieVersion")

    /** mock **/
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
    testImplementation ("org.mockito:mockito-core:3.12.4")
    testImplementation ("io.mockk:mockk:1.10.0")

    /** unit test **/
    testImplementation("junit:junit:$juniVersion")

    testImplementation("androidx.arch.core:core-testing:$archCoreVersion")
    testImplementation ("org.mockito:mockito-inline:3.11.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:junit:$junitExtVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$expressoCoreVersion")
}