package domain.db

import model.common.DbSortSelectable
import model.user.UserField
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.Instant
import model.user.User as CommonUser

object Users : Table<User>("user"), DbSortSelectable<UserField> {
    val id = int("id").primaryKey().bindTo { it.id }
    val email = varchar("email").bindTo { it.email }
    val password = varchar("password").bindTo { it.password }
    val username = varchar("username").bindTo { it.username }
    val createdAt = timestamp("created_at").bindTo { it.createdAt }

    override fun select(field: UserField): Column<*> = when (field) {
        UserField.Id -> id
        UserField.Email -> email
        UserField.Username -> username
    }
}

interface User : Entity<User> {
    val id: Int
    var email: String
    var password: String
    var username: String
    val createdAt: Instant
    val tags get() = UserTagXrefs.getList { it.userId eq id }.map { it.tag }

    fun toCommon(): CommonUser = CommonUser(
        id = id,
        email = email,
        username = username,
        tags = tags.map { it.toCommon() },
    )

    companion object : Entity.Factory<User>()
}