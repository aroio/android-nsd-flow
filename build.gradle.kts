// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.std}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}