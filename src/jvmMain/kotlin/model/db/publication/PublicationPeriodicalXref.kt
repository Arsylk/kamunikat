package model.db.publication

import model.db.periodical.Periodicals
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PublicationPeriodicalXrefs : Table("publication_periodical_xref") {
    val publicationId = reference("publication_id", Publications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val periodicalId = reference("periodical_id", Periodicals,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    override val primaryKey = PrimaryKey(publicationId, periodicalId)
}