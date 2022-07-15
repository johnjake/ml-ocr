// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion = "1.6.10"
    val spotlessVersion = "5.10.1"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

val installGitHooks by tasks.register(
    "installGitHooks",
    Copy::class.java
) {
    from(File(rootProject.rootDir, "pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks"))
    fileMode = 493 // 493 decimal == 755 octal; unix file permissions
}

tasks.getByPath("app:preBuild").dependsOn(installGitHooks)

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
