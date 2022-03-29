package model.db.catalog

import domain.db.idInt
import model.api.catalog.CatalogField
import model.common.DbSortable
import model.api.catalog.Catalog as CommonCatalog
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Catalogs : IntIdTable("catalog"), DbSortable<CatalogField> {
    val name = varchar("name", length = 31).uniqueIndex()
    val letter = char("letter").nullable()
    val hasInventory = bool("has_inventory").default(false)

    override fun select(field: CatalogField) = when (field) {
        CatalogField.Id -> id
        CatalogField.Name -> name
        CatalogField.Letter -> letter
        CatalogField.HasInventory -> hasInventory
    }
}

class Catalog(id: EntityID<Int>) : IntEntity(id) {
    var name by Catalogs.name
    var letter by Catalogs.letter
    var hasInventory by Catalogs.hasInventory

    companion object : IntEntityClass<Catalog>(Catalogs)
}

fun Catalog.toCommon() = CommonCatalog(
    id = idInt,
    name = name,
    letter = letter,
    hasInventory = hasInventory
)