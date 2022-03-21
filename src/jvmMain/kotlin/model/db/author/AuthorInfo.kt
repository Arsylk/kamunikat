package model.db.author

import domain.db.kDate
import kotlinx.datetime.LocalDate
import model.common.Gender
import org.ktorm.entity.Entity
import org.ktorm.schema.*

object AuthorInfos : Table<AuthorInfo>("author_info") {
    val authorId = int("author_id").primaryKey().references(Authors) { it.author }
    val gender = varchar("gender").transform({ Gender.mapFrom(it) }, { it.text }).bindTo { it.gender }
    val birthday = kDate("birthday").bindTo { it.birthday }
    val email = varchar("email").bindTo { it.email }
    val isAlive = boolean("is_alive").bindTo { it.isAlive }
    val isBirthdayActive = boolean("is_birthday_active").bindTo { it.isBirthdayActive }
    val isPublished = boolean("is_published").bindTo { it.isPublished }
}

interface AuthorInfo : Entity<AuthorInfo> {
    var author: Author
    var gender: Gender
    var birthday: LocalDate?
    var email: String?
    var isAlive: Boolean
    var isBirthdayActive: Boolean
    var isPublished: Boolean

    companion object : Entity.Factory<AuthorInfo>()
}