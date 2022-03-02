package domain.koin

import domain.auth.AuthService
import io.ktor.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import javax.sql.DataSource

internal inline val defaultModule get() =
    module {
         single { provideDatabase(get(named("primary"))) }
//        single {
//            Database.connect(
//                url = "jdbc:mysql://db:3306/database",
//                driver = "com.mysql.cj.jdbc.Driver",
//                user = "root",
//                password = "root",
//                dialect = MySqlDialect(),
//            )
//        }
         single(named("legacy")) { provideDatabase(get(named("legacy"))) }
//        single(named("legacy")) {
//            Database.connect(
//                url = "jdbc:mysql://db:3306/legacy",
//                driver = "com.mysql.cj.jdbc.Driver",
//                user = "root",
//                password = "root",
//                dialect = MySqlDialect()
//            )
//        }
        single { provideJwtTokenService(get()) }
    }



private fun provideDatabase(dataSource: DataSource) =
    Database.connect(dataSource)

private fun provideJwtTokenService(environment: ApplicationEnvironment) =
    AuthService(environment)