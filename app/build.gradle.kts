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
import AndroidConfigLib.materialVersion
import AndroidConfigLib.saveStateVersion
import AndroidConfigLib.timberVersion
import AndroidConfigLib.junitExtVersion
import AndroidConfigLib.cameraViewVersion
import AndroidConfigLib.mokitoCoreVersion
import AndroidConfigLib.textOcrVersion
import AndroidConfigLib.mokitoLineVersion
import AndroidConfigLib.lifecycleVersion
import AndroidConfigLib.mokitoVersion
import AndroidConfigLib.vMlifecyclerVersion
import AndroidConfigLib.serviceVersion
import AndroidConfigLib.beanVersion
import AndroidConfigLib.ktxActVersion
import AndroidConfigLib.liveDataVersion
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
    implementation ("androidx.activity:activity-ktx:$ktxActVersion")
    /** camera class **/

    implementation ("androidx.camera:camera-camera2:$cameraVersion")
    implementation ("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation ("androidx.camera:camera-view:$cameraViewVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$liveDataVersion")

    /** google machine learning text recognition **/
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:$textOcrVersion")

    /** lifecycle **/
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    /** lifecycle optional **/
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$vMlifecyclerVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("com.google.android.gms:play-services-cast-framework:$serviceVersion")
    implementation("org.beanshell:bsh:$beanVersion")
    implementation("com.jakewharton.timber:timber:$timberVersion")

    /** mock **/
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
    testImplementation ("org.mockito:mockito-core:$mokitoCoreVersion")
    testImplementation ("io.mockk:mockk:$mokitoVersion")

    /** unit test **/
    testImplementation("junit:junit:$juniVersion")

    testImplementation("androidx.arch.core:core-testing:$archCoreVersion")
    testImplementation ("org.mockito:mockito-inline:$mokitoLineVersion")
    androidTestImplementation ("androidx.test.ext:junit:$junitExtVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$expressoCoreVersion")
}