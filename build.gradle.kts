// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("kotlin_version")}")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.37")

    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {url = uri( "https://dl.bintray.com/kotlin/ktor") }
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
