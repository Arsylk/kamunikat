package model.db.author

import domain.db.idInt
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import model.api.author.AuthorAlias as CommonAuthorAlias

object AuthorAliases : IntIdTable("author_alias") {
    val authorId = reference("author_id", Authors,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val name = varchar("name", length = 127)
}

class AuthorAlias(id: EntityID<Int>) : IntEntity(id) {
    var author by Author referencedOn AuthorAliases.authorId
    var name by AuthorAliases.name

    companion object : IntEntityClass<AuthorAlias>(AuthorAliases)
}

fun AuthorAlias.toCommon() = CommonAuthorAlias(
    id = idInt,
    name = name,
)