package domain.db

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

object UserTagXrefs : Table<UserTagXref>("user_tag_xref") {
    val userId = int("user_id").references(Users) { it.user }
    val tagId = int("tag_id").references(UserTags) { it.tag }
}

interface UserTagXref : Entity<UserTagXref> {
    var user: User
    var tag: UserTag

    companion object : Entity.Factory<UserTagXref>()
}