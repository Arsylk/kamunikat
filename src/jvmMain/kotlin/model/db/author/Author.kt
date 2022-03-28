package model.db.author

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Authors : IntIdTable("author") {
    val name = varchar("name", length = 127).uniqueIndex()
}

class Author(id: EntityID<Int>) : IntEntity(id) {
    var name by Authors.name
    val aliases by AuthorAlias referrersOn AuthorAliases.authorId

    companion object : IntEntityClass<Author>(Authors)
}