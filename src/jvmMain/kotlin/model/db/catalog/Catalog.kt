package model.db.catalog

import model.api.catalog.CatalogField
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll


object Catalogs : IntIdTable("catalog") {
    val name = varchar("name", length = 31).uniqueIndex()
    val letter = char("letter").nullable()
    val hasInventory = bool("has_inventory").default(false)

    fun en(catalogField: CatalogField) {
        Catalog.all().orderBy(Catalogs.name to SortOrder.ASC)
    }
}

class Catalog(id: EntityID<Int>) : IntEntity(id) {
    var name by Catalogs.name
    var letter by Catalogs.letter
    var hasInventory by Catalogs.hasInventory

    companion object : IntEntityClass<Catalog>(Catalogs)
}