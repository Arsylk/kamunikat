package model.db.category

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Categories : IntIdTable("category") {
    val name = varchar("name", length = 63).uniqueIndex()
}

class Category(id: EntityID<Int>) : IntEntity(id) {
    var name by Categories.name

    companion object : IntEntityClass<Category>(Categories)
}