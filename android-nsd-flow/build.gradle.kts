plugins {
    `maven-publish`
    id("com.android.library")
    kotlin("android")
}

group = "com.github.aroio.nsd_flow"

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
    implementation(Dependencies.Android.annotation)

    testImplementation(Dependencies.JUnit.jupiter)
    testImplementation(Dependencies.Kotlin.Coroutines.test)
    testImplementation(Dependencies.Mockk.core)

    androidTestImplementation(Dependencies.Mockk.core)
    androidTestImplementation(Dependencies.Kotlin.Coroutines.test)
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
}
