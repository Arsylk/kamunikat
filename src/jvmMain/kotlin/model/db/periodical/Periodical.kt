package model.db.periodical

import domain.db.idInt
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import model.api.periodical.Periodical as CommonPeriodical

object Periodicals : IntIdTable("periodical") {
    val title = varchar("title", length = 255)
    val content = text("content").nullable()
    val isPublished = bool("is_published").default(false)
}

class Periodical(id: EntityID<Int>) : IntEntity(id) {
    var title by Periodicals.title
    var content by Periodicals.content
    var isPublished by Periodicals.isPublished

    companion object : IntEntityClass<Periodical>(Periodicals)
}

fun Periodical.toCommon() = CommonPeriodical(
    id = idInt,
    title = title,
    content = content,
    isPublished = isPublished,
)