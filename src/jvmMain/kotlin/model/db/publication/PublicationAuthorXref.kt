package model.db.publication

import model.db.author.Authors
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PublicationAuthorXrefs : Table("publication_author_xref") {
    val publicationId = reference("publication_id", Publications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val authorId = reference("author_id", Authors,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    override val primaryKey = PrimaryKey(publicationId, authorId)
}