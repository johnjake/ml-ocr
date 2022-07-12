import ConfigFlavor.cameraSystem
import ConfigFlavor.fileSystem
import ConfigFlavor.greenFlavor
import ConfigFlavor.redFlavor

object AndroidConfigLib {
    const val ktxVersion = "1.7.0"
    const val lottieVersion = "5.2.0"
    const val compatVersion = "1.4.2"
    const val materialVersion = "1.6.1"
    const val constrainVersion = "2.1.4"
    const val daggerVersion = "2.38.1"
    const val compilerVersion = "2.38.1"
    const val hiltViewModelVersion = "1.0.0-alpha03"
    const val lifeCycleVersion = "2.6.0-alpha01"
    const val saveStateVersion = "2.6.0-alpha01"
    const val cameraVersion = "1.0.2"
    const val timberVersion = "4.7.1"
    const val mokitoCoreVersion = "4.6.1"
    const val mokitoVersion = "1.10.0"
    const val juniVersion = "4.13.2"
    const val archCoreVersion = "2.1.0"
    const val junitExtVersion = "1.1.3"
    const val expressoCoreVersion = "3.4.0"
    const val coroutineVersion = "1.6.1"
}

object ConfigFlavor {
    const val redFlavor = "red"
    const val greenFlavor = "green"
    const val cameraSystem = "builtincamera"
    const val fileSystem = "filesystem"
}

object FlavorDimension {
    const val typeDimension = "typeDimen"
    const val colorDimension = "colorDimen"
}

object FlavorVersion {
    const val redFileSystem = "app-red-filesystem"
    const val redCameraSystem = "app-red-built-in-camera"
    const val greenFileSystem = "app-green-filesystem"
    const val greenCameraSystem = "app-green-built-in-camera"

    fun appNameApk(name: String): String {
        return "$name.apk"
    }
}

object SourceSet {
    const val redDirJava = "src/$redFlavor/java"
    const val redDirRes = "src/$redFlavor/res"
    const val greenDirJava = "src/$greenFlavor/java"
    const val greenDirRes = "src/$greenFlavor/res"
    const val cameraDirJava = "src/$cameraSystem/java"
    const val cameraDirRes = "src/$cameraSystem/res"
    const val fileDirJava = "src/$fileSystem/java"
    const val fileDirRes = "src/$fileSystem/res"
}
