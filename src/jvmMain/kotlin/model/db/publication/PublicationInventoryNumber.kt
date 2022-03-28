package model.db.publication

import model.db.catalog.Catalog
import model.db.catalog.Catalogs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PublicationInventoryNumbers : IntIdTable("publication_inventory_number") {
    val publicationId = reference("publication_id", Publications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val catalogId = reference("catalog_id", Catalogs,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val text = varchar("text", length = 15)
}

class PublicationInventoryNumber(id: EntityID<Int>) : IntEntity(id) {
    var publication by Publication referencedOn PublicationInventoryNumbers.publicationId
    var catalog by Catalog referencedOn PublicationInventoryNumbers.catalogId
    var text by PublicationInventoryNumbers.text

    companion object : IntEntityClass<PublicationInventoryNumber>(PublicationInventoryNumbers)
}