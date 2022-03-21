package domain.db

import org.koin.mp.KoinPlatformTools
import org.ktorm.database.Database
import org.ktorm.entity.*
import org.ktorm.schema.BaseTable
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table


inline fun <Entity : Any, Table: BaseTable<Entity>> Table.getList(
    predicate: (Table) -> ColumnDeclaring<Boolean>,
): List<Entity> {
    val db: Database = KoinPlatformTools.defaultContext().get().get()
    return db.sequenceOf(this).filter(predicate).toList()
}