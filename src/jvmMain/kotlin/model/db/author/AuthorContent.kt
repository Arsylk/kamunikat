package model.db.author

import model.common.Language
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object AuthorContents : IntIdTable("author_content") {
    val authorId = reference("author_id", Authors,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val language = enumerationByName("language", 31, Language::class).default(Language.Belarusian)
    val content = text("content")
}

class AuthorContent(id: EntityID<Int>) : IntEntity(id) {
    var author by Author referencedOn AuthorContents.authorId
    var language by AuthorContents.language
    var content by AuthorContents.content

    companion object : IntEntityClass<AuthorContent>(AuthorContents)
}