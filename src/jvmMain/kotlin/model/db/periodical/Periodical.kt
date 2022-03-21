package model.db.periodical

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Periodicals : Table<Periodical>("periodical") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

interface Periodical : Entity<Periodical> {
    val id: Int
    var name: String

    companion object : Entity.Factory<Periodical>()
}