package model.db.author

import model.common.Gender
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date


object AuthorInfos : IntIdTable("author_info") {
    val authorId = reference("author_id", Authors,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val gender = enumerationByName("gender", 31, Gender::class).default(Gender.Unknown)
    val birthday = date("birthday").nullable()
    val email = varchar("email", length = 63).nullable()
    val isAlive = bool("is_alive")
    val isBirthdayActive = bool("is_birthday_active")
    val isPublished = bool("is_published")
}

class AuthorInfo(id: EntityID<Int>) : IntEntity(id) {
    var author by Author referencedOn AuthorInfos.authorId
    var gender by AuthorInfos.gender
    var birthday by AuthorInfos.birthday
    var email by AuthorInfos.email
    var isAlive by AuthorInfos.isAlive
    var isBirthdayActive by AuthorInfos.isBirthdayActive
    var isPublished by AuthorInfos.isPublished
    val contents by AuthorContent referrersOn AuthorContents.authorId

    companion object : IntEntityClass<AuthorInfo>(AuthorInfos)
}