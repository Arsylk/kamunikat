package model.db.author

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object AuthorAliases : Table<AuthorAlias>("author_alias") {
    val id = int("id").primaryKey().bindTo { it.id }
    val authorId = int("author_id").primaryKey().references(Authors) { it.author }
    val name = varchar("name").bindTo { it.name }
}

interface AuthorAlias : Entity<AuthorAlias> {
    val id: Int
    var author: Author
    var name: String

    companion object : Entity.Factory<AuthorAlias>()
}