package domain.db

import model.common.DbSortSelectable
import model.user.UserTagField
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import model.user.UserTag as CommonUserTag

object UserTags : Table<UserTag>("user_tag"), DbSortSelectable<UserTagField> {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }

    override fun select(field: UserTagField) =
        when (field) {
            UserTagField.Id -> id
            UserTagField.Name -> name
        }
}

interface UserTag : Entity<UserTag> {
    val id: Int
    var name: String

    fun toCommon(): CommonUserTag = CommonUserTag(
        id = id,
        name = name,
    )

    companion object : Entity.Factory<UserTag>()
}