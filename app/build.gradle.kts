import AndroidConfigLib.archCoreVersion
import AndroidConfigLib.beanVersion
import AndroidConfigLib.cameraVersion
import AndroidConfigLib.cameraViewVersion
import AndroidConfigLib.compatVersion
import AndroidConfigLib.compilerVersion
import AndroidConfigLib.constrainVersion
import AndroidConfigLib.coroutineVersion
import AndroidConfigLib.daggerVersion
import AndroidConfigLib.expressoCoreVersion
import AndroidConfigLib.hiltViewModelVersion
import AndroidConfigLib.juniVersion
import AndroidConfigLib.junitExtVersion
import AndroidConfigLib.ktxActVersion
import AndroidConfigLib.ktxVersion
import AndroidConfigLib.lifeCycleVersion
import AndroidConfigLib.lifecycleVersion
import AndroidConfigLib.liveDataVersion
import AndroidConfigLib.materialVersion
import AndroidConfigLib.mokitoCoreVersion
import AndroidConfigLib.mokitoLineVersion
import AndroidConfigLib.mokitoVersion
import AndroidConfigLib.saveStateVersion
import AndroidConfigLib.serviceVersion
import AndroidConfigLib.textOcrVersion
import AndroidConfigLib.timberVersion
import AndroidConfigLib.vMlifecyclerVersion
import ConfigFlavor.cameraSystem
import ConfigFlavor.fileSystem
import ConfigFlavor.greenFlavor
import ConfigFlavor.redFlavor
import FlavorDimension.colorDimension
import FlavorDimension.typeDimension
import FlavorVersion.greenFileSystem
import FlavorVersion.redCameraSystem
import FlavorVersion.redFileSystem
import SourceSet.cameraDirJava
import SourceSet.cameraDirRes
import SourceSet.fileDirJava
import SourceSet.fileDirRes
import SourceSet.greenDirJava
import SourceSet.greenDirRes
import SourceSet.redDirJava
import SourceSet.redDirRes
import org.jetbrains.kotlin.builtins.StandardNames.FqNames.target


plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.diffplug.spotless")
}

apply(from = "../ktlint.gradle")

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
            applicationIdSuffix = ".camera"
            versionName = redCameraSystem
        }
        create(fileSystem) {
            dimension = typeDimension
            applicationIdSuffix = ".file"
            versionName = greenFileSystem
        }

        create(redFlavor) {
            dimension = colorDimension
            applicationIdSuffix = ".red"
            versionName = redFileSystem
        }
        create(greenFlavor) {
            dimension = colorDimension
            applicationIdSuffix = ".green"
            versionName = greenFileSystem
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
        this.getByName(redFlavor) {
            this.java.srcDir(redDirJava)
        }
        this.getByName(redFlavor) {
            this.res.srcDir(redDirRes)
        }

        this.getByName(greenFlavor) {
            this.java.srcDir(greenDirJava)
        }
        this.getByName(greenFlavor) {
            this.res.srcDir(greenDirRes)
        }

        this.getByName(cameraSystem) {
            this.java.srcDir(cameraDirJava)
        }
        this.getByName(cameraSystem) {
            this.res.srcDir(cameraDirRes)
        }

        this.getByName(fileSystem) {
            this.java.srcDir(fileDirJava)
        }
        this.getByName(fileSystem) {
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

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.42.1")
    }
    kotlinGradle {
        target("*.gradle.kts", "additionalScripts/*.gradle.kts")
        ktlint("0.42.1")
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
    implementation("androidx.activity:activity-ktx:$ktxActVersion")
    /** camera class **/

    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraViewVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$liveDataVersion")

    /** google machine learning text recognition **/
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:$textOcrVersion")

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
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
    testImplementation("org.mockito:mockito-core:$mokitoCoreVersion")
    testImplementation("io.mockk:mockk:$mokitoVersion")

    /** unit test **/
    testImplementation("junit:junit:$juniVersion")

    testImplementation("androidx.arch.core:core-testing:$archCoreVersion")
    testImplementation("org.mockito:mockito-inline:$mokitoLineVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitExtVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$expressoCoreVersion")
}
