package model.db.category

import domain.db.idInt
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import model.api.category.Category as CommonCategory

object Categories : IntIdTable("category") {
    val name = varchar("name", length = 63).uniqueIndex()
}

class Category(id: EntityID<Int>) : IntEntity(id) {
    var name by Categories.name

    companion object : IntEntityClass<Category>(Categories)
}

fun Category.toCommon() = CommonCategory(
    id = idInt,
    name = name,
)