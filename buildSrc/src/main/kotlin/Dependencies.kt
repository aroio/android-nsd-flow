object Dependencies {

    object Android {
        const val annotation = "androidx.annotation:annotation:${Versions.Android.annotation}"
    }

    object Kotlin {
        const val std = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin.std}"

        object Coroutines {
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.Coroutines.core}"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.Coroutines.core}"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Kotlin.Coroutines.core}"
        }
    }

    object JUnit {
        const val jupiter = "org.junit.jupiter:junit-jupiter:${Versions.JUnit.jupiter}"
    }

    object Mockk {
        const val core = "io.mockk:mockk:${Versions.Mockk.core}"
    }
}