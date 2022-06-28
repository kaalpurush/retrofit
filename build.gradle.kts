// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.1.3")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("kotlin_version")}")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.2")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.41")

    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
