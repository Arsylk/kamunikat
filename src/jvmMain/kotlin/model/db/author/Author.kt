package model.db.author

import domain.db.idInt
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import model.api.author.Author as CommonAuthor

object Authors : IntIdTable("author") {
    val name = varchar("name", length = 127).uniqueIndex()
}

class Author(id: EntityID<Int>) : IntEntity(id) {
    var name by Authors.name
    val aliases by AuthorAlias referrersOn AuthorAliases.authorId
    val info by AuthorInfo optionalBackReferencedOn AuthorInfos.authorId

    companion object : IntEntityClass<Author>(Authors)
}

fun Author.toCommon() = CommonAuthor(
    id = idInt,
    name = name,
    aliases = aliases.map { it.toCommon() },
    hasInfo = info != null,
)