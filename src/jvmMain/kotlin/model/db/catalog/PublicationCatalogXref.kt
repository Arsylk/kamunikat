package model.db.catalog

import model.db.publication.Publication
import model.db.publication.Publications
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

object PublicationCatalogXrefs : Table<PublicationCatalogXref>("publication_catalog") {
    val publicationId = int("publication_id").references(Publications) { it.publication }
    val catalogId = int("catalog_id").references(Catalogs) { it.catalog }
}

interface PublicationCatalogXref : Entity<PublicationCatalogXref> {
    var publication: Publication
    var catalog: Catalog

    companion object : Entity.Factory<PublicationCatalogXref>()
}