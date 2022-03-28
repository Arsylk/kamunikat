package model.db.publication

import model.db.catalog.Catalogs
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PublicationCatalogXrefs : Table("publication_catalog") {
    val publicationId = reference("publication_id", Publications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val catalogId = reference("catalog_id", Catalogs,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    override val primaryKey = PrimaryKey(publicationId, catalogId)
}