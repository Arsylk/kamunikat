package domain.koin

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource
import org.apache.naming.factory.DataSourceLinkFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.sql.ConnectionBuilder
import javax.naming.Context
import javax.naming.InitialContext
import javax.sql.ConnectionPoolDataSource
import javax.sql.DataSource
import javax.sql.PooledConnection
import javax.sql.PooledConnectionBuilder

internal inline val datasourceModule get() =
    module {
        single(named("primary")) { provideDatasource() }
        single(named("legacy")) { provideLegacyDatasource() }
    }

private fun provideDatasource(): DataSource {
    return try {
        val initContext = InitialContext()
        val envContext = initContext.lookup("java:comp/env") as Context
        envContext.lookup("jdbc/db_primary") as DataSource
    }catch (_: Throwable) {
        // TODO debug environment only
        MysqlConnectionPoolDataSource().apply {
            setURL("jdbc:mysql://db:3306/database")
            user = "root"
            password = "root"
        }
    }
}

private fun provideLegacyDatasource(): DataSource {
    return try {
        val initContext = InitialContext()
        val envContext = initContext.lookup("java:comp/env") as Context
        envContext.lookup("jdbc/db_legacy") as DataSource
    } catch (_: Throwable) {
        // TODO debug environment only
        MysqlConnectionPoolDataSource().apply {
            setURL("jdbc:mysql://db:3306/legacy")
            user = "root"
            password = "root"
        }
    }
}