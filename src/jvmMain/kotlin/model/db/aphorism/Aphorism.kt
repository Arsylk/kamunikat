package model.db.aphorism

import model.db.author.Author
import model.db.author.Authors
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Aphorisms : IntIdTable("aphorism") {
    val title = varchar("title", length = 127)
    val content = text("content")
    val authorId = optReference("author_id", Authors,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.CASCADE,
    )
    val isPublished = bool("is_published").default(false)
}

class Aphorism(id: EntityID<Int>) : IntEntity(id) {
    var title by Aphorisms.title
    var content by Aphorisms.content
    var author by Author optionalReferencedOn Aphorisms.authorId
    var isPublished by Aphorisms.isPublished

    companion object : IntEntityClass<Aphorism>(Aphorisms)
}