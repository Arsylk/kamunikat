package model.db.user

import domain.db.idInt
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import model.user.UserTag as CommonUserTag

object UserTags : IntIdTable("user_tag") {
    val name = varchar("name", length = 31).uniqueIndex()
}

class UserTag(id: EntityID<Int>) : IntEntity(id) {
    var name by UserTags.name

    fun toCommon() = CommonUserTag(
        id = idInt,
        name = name,
    )

    companion object : IntEntityClass<UserTag>(UserTags)
}