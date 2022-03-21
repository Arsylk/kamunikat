package model.db.category

import model.db.publication.Publication
import model.db.publication.Publications
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

object PublicationCategoryXrefs : Table<PublicationCategoryXref>("publication_category_xref") {
    val publicationId = int("publication_id").references(Publications) { it.publication }
    val categoryId = int("category_id").references(Categories) { it.category }
}

interface PublicationCategoryXref : Entity<PublicationCategoryXref> {
    var publication: Publication
    var category: Category

    companion object : Entity.Factory<PublicationCategoryXref>()
}