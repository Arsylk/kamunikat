@file:JvmName("KoinServiceJvmKt")
package domain.koin

import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.jvm.JvmName


operator fun KoinApplication.plusAssign(module: Module) { modules(module) }

fun KoinApplication.setupKoin() {
    this += module(createdAtStart = true) {
        single {
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
                encodeDefaults = true
            }
        }
    }

    setupKoinPlatform()
}

expect fun KoinApplication.setupKoinPlatform()
