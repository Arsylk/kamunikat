package model.db.user

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import model.user.User as CommonUser

object Users : IntIdTable("user") {
    val email = varchar("email", length = 63)
    val password = varchar("password", length = 31)
    val username = varchar("username", length = 31)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
}

class User(id: EntityID<Int>) : IntEntity(id) {
    var email by Users.email
    var password by Users.password
    var username by Users.username
    var createdAt by Users.createdAt
    var tags by UserTag via UserTagXrefs

    fun toCommon() = CommonUser(
        id = id.value,
        email = email,
        username = username,
        tags = tags.map { it.toCommon() },
    )

    companion object : IntEntityClass<User>(Users)
}