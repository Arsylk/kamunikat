package domain.koin

import domain.auth.AuthService
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module
import javax.sql.DataSource

internal inline val defaultModule get() =
    module {
        single { provideDatabase(get(named("primary"))) }
        single(named("legacy")) { provideDatabase(get(named("legacy"))) }
        single { provideJwtTokenService(get()) }
    }



private fun provideDatabase(dataSource: DataSource) =
    Database.connect(
        datasource = dataSource,
        databaseConfig = DatabaseConfig {
            keepLoadedReferencesOutOfTransaction = true
        },
    )

private fun provideJwtTokenService(environment: ApplicationEnvironment) =
    AuthService(environment)