package domain.db

import io.ktor.util.*
import kotlinx.datetime.*
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import java.sql.*

object KotlinLocalDateSqlType : SqlType<LocalDate>(Types.DATE, "k_date") {
    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: LocalDate) {
        ps.setDate(index, Date.valueOf(parameter.toJavaLocalDate()))
    }

    override fun doGetResult(rs: ResultSet, index: Int): LocalDate? {
        return rs.getDate(index)?.toLocalDate()?.toKotlinLocalDate()
    }
}

fun BaseTable<*>.kDate(name: String): Column<LocalDate> {
    return registerColumn(name, KotlinLocalDateSqlType)
}

object KotlinLocalDateTimeSqlType : SqlType<LocalDateTime>(Types.TIMESTAMP, "k_datetime") {
    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: LocalDateTime) {
        ps.setTimestamp(index, Timestamp.valueOf(parameter.toJavaLocalDateTime()))
    }

    override fun doGetResult(rs: ResultSet, index: Int): LocalDateTime? {
        return rs.getTimestamp(index)?.toLocalDateTime()?.toKotlinLocalDateTime()
    }
}

fun BaseTable<*>.kDatetime(name: String): Column<LocalDateTime> {
    return registerColumn(name, KotlinLocalDateTimeSqlType)
}