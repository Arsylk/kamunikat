package domain.db

import model.common.DbSortSelectable
import model.user.UserField
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

    override val default = UserField.Id
    override fun select(field: UserField): Column<*> = when (field) {
        UserField.Id -> id
        UserField.Email -> email
        UserField.Username -> username
    }
}

interface User : Entity<User> {
    val id: Int
    val email: String
    val password: String
    val username: String
    val createdAt: Instant

    fun toCommonUser(): CommonUser = CommonUser(
        id = id,
        email = email,
        username = username,
    )
}