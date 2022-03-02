package domain.koin

import org.koin.core.KoinApplication

actual fun KoinApplication.setupKoinPlatform() {
    modules(
        datasourceModule,
        defaultModule,
    )
}