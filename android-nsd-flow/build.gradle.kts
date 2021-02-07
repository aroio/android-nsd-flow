plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka-android")
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

android {
    setCompileSdkVersion(30)
    defaultConfig {
        setMinSdkVersion(19)
        setTargetSdkVersion(30)
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        // Enabling multidex support.
        multiDexEnabled = true
    }
    buildTypes {
        getByName("debug") {
            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    sourceSets {
        getByName("test") {
            java.srcDir("src/sharedTest/java")
        }
        getByName("androidTest") {
            java.srcDir("src/sharedTest/java")
        }
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}

dependencies {
    implementation(Dependencies.Kotlin.std)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation("androidx.annotation:annotation:1.1.0")

    testImplementation("org.mockito:mockito-core:2.28.2")
    testImplementation("junit:junit:4.13.1")
    testImplementation(Dependencies.Kotlin.Coroutines.test)

    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("org.mockito:mockito-android:2.28.2")
}
