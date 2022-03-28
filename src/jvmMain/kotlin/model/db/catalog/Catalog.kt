package model.db.catalog

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Catalogs : IntIdTable("catalog") {
    val name = varchar("name", length = 31).uniqueIndex()
    val letter = char("letter").nullable()
    val hasInventory = bool("has_inventory").default(false)
}

class Catalog(id: EntityID<Int>) : IntEntity(id) {
    var name by Catalogs.name
    var letter by Catalogs.letter
    var hasInventory by Catalogs.hasInventory

    companion object : IntEntityClass<Catalog>(Catalogs)
}