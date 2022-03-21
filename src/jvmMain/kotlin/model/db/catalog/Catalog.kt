package model.db.catalog

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Catalogs : Table<Catalog>("catalog") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

interface Catalog : Entity<Catalog> {
    val id: Int
    var name: String

    companion object : Entity.Factory<Catalog>()
}