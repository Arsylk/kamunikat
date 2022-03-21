package model.db.author

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Authors : Table<Author>("author") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

interface Author : Entity<Author> {
    val id: Int
    var name: String

    companion object : Entity.Factory<Author>()
}