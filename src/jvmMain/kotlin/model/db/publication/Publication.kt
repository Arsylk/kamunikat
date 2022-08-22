package model.db.publication

import domain.db.createdAt
import domain.db.idInt
import domain.db.updatedAt
import model.db.author.Author
import model.db.catalog.Catalog
import model.db.category.Category
import model.db.periodical.Periodical
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import model.api.publication.Publication as CommonPublication

object Publications : IntIdTable("publication") {
    val isPublished = bool("is_published").default(false)
    val isPeriodical = bool("is_periodical").default(false)
    val isPlanned = bool("is_planned").default(false)
    val isLicenced = bool("is_licenced").default(false)
    val position = integer("position").default(0)

    val title = varchar("title", length = 255).index()
    val subtitle = varchar("subtitle", length = 255)
    val originalTitle = varchar("original_title", length = 255)
    val coAuthor = varchar("co_author", length = 255)
    val translator = varchar("translator", length = 255)

    val edition = varchar("edition", length = 63)
    val volume = varchar("volume", length = 31)
    val redactor = varchar("redactor", length = 63)
    val college = varchar("college", length = 255)
    val illustrator = varchar("illustrator", length = 255)

    val place = varchar("place", length = 127)
    val series = varchar("series", length = 255)
    val year = date("year").nullable()
    val publishingHouse = varchar("publishing_house", length = 255)
    val publisher = varchar("publisher", length = 511)
    val dimensions = varchar("dimensions", length = 31)

    val signature = varchar("signature", length = 31)
    val isbn = varchar("isbn", length = 63)
    val issn = varchar("issn", length = 63)
    val ukd = varchar("ukd", length = 255)
    val copyright = varchar("copyright", length = 255)
    val shopUrl = varchar("shop_url", length = 255)
    val origin = varchar("origin", length = 255)

    val extra = text("extra")
    val description = text("description")

    val legacyAuthor = varchar("legacy_author", length = 255).default("")
    val legacyCatalog = varchar("legacy_catalog", length = 127).default("")
    val legacyInventoryNumber = varchar("legacy_inventory_number", length = 127).default("")

    val createdAt = createdAt()
    val updatedAt = updatedAt()
}

class Publication(id: EntityID<Int>) : IntEntity(id) {
    var isPublished by Publications.isPublished
    var isPeriodical by Publications.isPeriodical
    var isPlanned by Publications.isPlanned
    var isLicenced by Publications.isLicenced
    var position by Publications.position

    var title by Publications.title
    var subtitle by Publications.subtitle
    var originalTitle by Publications.originalTitle
    var coAuthor by Publications.coAuthor
    var translator by Publications.translator

    var edition by Publications.edition
    var volume by Publications.volume
    var redactor by Publications.redactor
    var college by Publications.college
    var illustrator by Publications.illustrator

    var place by Publications.place
    var series by Publications.series
    var year by Publications.year
    var publishingHouse by Publications.publishingHouse
    var publisher by Publications.publisher
    var dimensions by Publications.dimensions

    var signature by Publications.signature
    var isbn by Publications.isbn
    var issn by Publications.issn
    var ukd by Publications.ukd
    var copyright by Publications.copyright
    var shopUrl by Publications.shopUrl
    var origin by Publications.origin

    var extra by Publications.extra
    var description by Publications.description

    var legacyAuthor by Publications.legacyAuthor
    var legacyCatalog by Publications.legacyCatalog
    var legacyInventoryNumber by Publications.legacyInventoryNumber

    var periodicals by Periodical via PublicationPeriodicalXrefs
    var catalogs by Catalog via PublicationCatalogXrefs
    var categories by Category via PublicationCategoryXrefs
    var authors by Author via PublicationAuthorXrefs
    val inventoryNumbers by PublicationInventoryNumber referrersOn PublicationInventoryNumbers.publicationId

    var createdAt by Publications.createdAt
    var updatedAt by Publications.updatedAt

    companion object : IntEntityClass<Publication>(Publications)
}

fun Publication.toCommon() = CommonPublication(
    id = idInt,
    isPublished = isPublished,
    isPeriodical = isPeriodical,
    isPlanned = isPlanned,
    isLicenced = isLicenced,
    position = position,
    title = title,
    subtitle = subtitle,
    originalTitle = originalTitle,
    coAuthor = coAuthor,
    translator = translator,
    edition = edition,
    volume = volume,
    redactor = redactor,
    college = college,
    illustrator = illustrator,
    place = place,
    series = series,
    year = year,
    publishingHouse = publishingHouse,
    publisher = publisher,
    dimensions = dimensions,
    signature = signature,
    isbn = isbn,
    issn = issn,
    ukd = ukd,
    copyright = copyright,
    shopUrl = shopUrl,
    origin = origin,
    extra = extra,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt
)