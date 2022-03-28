package model.db.user

import org.jetbrains.exposed.sql.Table

object UserTagXrefs : Table("user_tag_xref") {
    val userId = reference("user_id", Users)
    val tagId = reference("tag_id", UserTags)
    override val primaryKey = PrimaryKey(userId, tagId)
}