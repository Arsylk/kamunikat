package model.db.publication

import model.db.category.Categories
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PublicationCategoryXrefs : Table("publication_category_xref") {
    val publicationId = reference("publication_id", Publications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val categoryId = reference("category_id", Categories,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    override val primaryKey = PrimaryKey(publicationId, categoryId)
}