package model.db.category

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Categories : Table<Category>("category") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

interface Category : Entity<Category> {
    var id: Int
    var name: String

    companion object : Entity.Factory<Category>()
}