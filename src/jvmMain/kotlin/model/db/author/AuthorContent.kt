package model.db.author

import model.common.Language
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar

object AuthorContents : Table<AuthorContent>("author_content") {
    val id = int("id").primaryKey()
    val authorId = int("author_id").references(Authors) { it.author }
    val language = varchar("language").transform({ Language.mapFrom(it) }, { it.text }).bindTo { it.language }
    val content = text("content").bindTo { it.content }
}

interface AuthorContent : Entity<AuthorContent> {
    val id: Int
    var author: Author
    var language: Language
    var content: String

    companion object : Entity.Factory<AuthorContent>()
}