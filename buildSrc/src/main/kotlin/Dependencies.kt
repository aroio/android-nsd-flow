object Dependencies {
    object Kotlin {
        const val std = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin.std}"

        object Coroutines {
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.Coroutines.core}"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Kotlin.Coroutines.core}"
        }
    }
}