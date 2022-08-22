package domain.db

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.TransactionManager

const val CREATED_AT_NAME = "created_at"
const val UPDATED_AT_NAME = "updated_at"

fun Table.createdAt() = timestamp(CREATED_AT_NAME).defaultExpression(CurrentTimestamp())

fun Table.updatedAt() = timestamp(UPDATED_AT_NAME).defaultExpression(CurrentTimestamp())


fun <T> SchemaUtils.changeUpdatedAt(table: Table, column: Column<T>) {
    with(TransactionManager.current()) {
        val currentTimestamp = CurrentTimestamp<T>()
        val tableName = table.nameInDatabaseCase()
        val columnName = column.nameInDatabaseCase()
        val columnType = column.columnType.sqlType()
        val nullable = column.columnType.nullable

        val query = buildString {
            append("ALTER TABLE `$tableName` CHANGE `$columnName` ")
            append("`$columnName` $columnType ")
            append(if (nullable) "NULL " else "NOT NULL ")
            append("DEFAULT $currentTimestamp ")
            append("ON UPDATE $currentTimestamp")
        }
        exec(query)
    }
}