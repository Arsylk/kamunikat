package model.db.publication

import domain.db.kDate
import domain.db.kDatetime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import model.db.periodical.Periodical
import model.db.periodical.Periodicals
import org.ktorm.entity.Entity
import org.ktorm.schema.*

object Publications : Table<Publication>("publication") {
    val id = int("id").primaryKey().bindTo { it.id }
    val isPublished = boolean("is_published").bindTo { it.isPublished }
    val isPeriodical = boolean("is_periodical").bindTo { it.isPeriodical }
    val isPlanned = boolean("is_planned").bindTo { it.isPlanned }
    val isLicenced = boolean("is_licenced").bindTo { it.isLicenced }
    val position = int("position").bindTo { it.position }

    val title = varchar("title").bindTo { it.title }
    val subtitle = varchar("subtitle").bindTo { it.subtitle }
    val originalTitle = varchar("original_title").bindTo { it.originalTitle }
    val coAuthor = varchar("co_author").bindTo { it.coAuthor }
    val translator = varchar("translator").bindTo { it.translator }

    val edition = varchar("edition").bindTo { it.edition }
    val volume = varchar("volume").bindTo { it.volume }
    val redactor = varchar("redactor").bindTo { it.redactor }
    val college = varchar("college").bindTo { it.college }
    val illustrator = varchar("translator").bindTo { it.illustrator }

    val place = varchar("place").bindTo { it.place }
    val series = varchar("series").bindTo { it.series }
    val year = kDate("year").bindTo { it.year }
    val publishingHouse = varchar("publishing_house").bindTo { it.publishingHouse }
    val publisher = varchar("publisher").bindTo { it.publisher }
    val dimensions = varchar("dimensions").bindTo { it.dimensions }

    val signature = varchar("signature").bindTo { it.signature }
    val isbn = varchar("isbn").bindTo { it.isbn }
    val issn = varchar("issn").bindTo { it.issn }
    val ukd = varchar("ukd").bindTo { it.ukd }
    val copyright = varchar("copyright").bindTo { it.copyright }
    val shopUrl = varchar("shop_url").bindTo { it.shopUrl }
    val source = varchar("source").bindTo { it.source }

    val extra = text("extra").bindTo { it.extra }
    val description = text("description").bindTo { it.description }

    val periodicalId = int("periodical_id").references(Periodicals) { it.periodical }

    val legacyAuthor = varchar("legacy_author").bindTo { it.legacyAuthor }
    val legacyCatalog = varchar("legacy_catalog").bindTo { it.legacyCatalog }
    val legacyInventoryNumber = varchar("legacy_inventory_number").bindTo { it.legacyInventoryNumber }

    val createdAt = kDatetime("created_at").bindTo { it.createdAt }
    val updatedAt = kDatetime("updated_at").bindTo { it.updatedAt }
}

interface Publication : Entity<Publication> {
    var id: Int
    var isPublished: Boolean
    var isPeriodical: Boolean
    var isPlanned: Boolean
    var isLicenced: Boolean
    var position: Int

    var title: String
    var subtitle: String?
    var originalTitle: String?
    var coAuthor: String?
    var translator: String?

    var edition: String?
    var volume: String?
    var redactor: String?
    var college: String?
    var illustrator: String?

    var place: String?
    var series: String?
    var year: LocalDate?
    var publishingHouse: String?
    var publisher: String?
    var dimensions: String?

    var signature: String?
    var isbn: String?
    var issn: String?
    var ukd: String?
    var copyright: String?
    var shopUrl: String?
    var source: String?

    var extra: String?
    var description: String?

    var periodical: Periodical?
    var legacyAuthor: String
    var legacyCatalog: String
    var legacyInventoryNumber: String

    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime

    companion object : Entity.Factory<Publication>()
}