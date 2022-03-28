package test

import domain.db.new
import domain.db.rawExec
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import model.db.author.Author
import model.db.author.Authors
import model.db.catalog.Catalog
import model.db.catalog.Catalogs
import model.db.category.Categories
import model.db.category.Category
import model.db.periodical.Periodical
import model.db.periodical.Periodicals
import model.db.publication.Publication
import model.db.publication.PublicationInventoryNumber
import model.db.publication.PublicationInventoryNumbers
import model.db.publication.Publications
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object MigratePublications : IMigrate {
    /**
     * Requires:
     *  - [MigrateAuthors]
     *  - [MigrateCatalogs]
     *  - [MigrateCategories]
     * */
    override fun execute(db: Database, legacy: Database) {
        migratePublications(db, legacy)
        migrateInventoryNumbers(db)
    }

    private fun migratePublications(db: Database, legacy: Database) {
        legacy.rawExec(PUBLICATIONS_QUERY) { row ->
            val authors = row.getString("legacy_author").split(",")
                .map { it.trim().lowercase() }
            val catalogs = row.getString("legacy_catalog").split(",")
                .map { it.trim().lowercase() }
            val categoryIds = row.getString("category_ids").split(",")
                .mapNotNull { it.toIntOrNull() }.toSet()

            val id = row.getInt("id")
            println("building: Id($id)")
            Publication.new(db, id) {
                isPublished = row.getBoolean("is_published")
                isPeriodical = row.getBoolean("is_periodical")
                isPlanned = row.getBoolean("is_planned")
                isLicenced = row.getBoolean("is_licenced")
                position = row.getInt("position")

                title = row.getString("title").orEmpty()
                subtitle = row.getString("subtitle").orEmpty()
                originalTitle = row.getString("original_title").orEmpty()
                coAuthor = row.getString("co_author").orEmpty()
                translator = row.getString("translator").orEmpty()

                edition = row.getString("edition").orEmpty()
                volume = row.getString("volume").orEmpty()
                redactor = row.getString("redactor").orEmpty()
                college = row.getString("college").orEmpty()
                illustrator = row.getString("illustrator").orEmpty()

                place = row.getString("place").orEmpty()
                series = row.getString("series").orEmpty()
                year = parseRawYear(row.getString("raw_year").orEmpty())
                publishingHouse = row.getString("publishing_house").orEmpty()
                publisher = row.getString("publisher").orEmpty()
                dimensions = row.getString("dimensions").orEmpty()

                signature = row.getString("signature").orEmpty()
                isbn = row.getString("isbn").orEmpty()
                issn = row.getString("issn").orEmpty()
                ukd = row.getString("ukd").orEmpty()
                copyright = row.getString("copyright").orEmpty()
                shopUrl = row.getString("shop_url").orEmpty()
                origin = row.getString("origin").orEmpty()

                extra = row.getString("extra").orEmpty()
                description = row.getString("description").orEmpty()

                legacyAuthor = row.getString("legacy_author").orEmpty()
                legacyCatalog = row.getString("legacy_catalog").orEmpty()
                legacyInventoryNumber = row.getString("legacy_inventory_number").orEmpty()

                this.periodicals = emptySized()
                this.catalogs = Catalog.find { Catalogs.name.lowerCase() inList catalogs }
                this.categories = Category.find { Categories.id inList categoryIds }
                this.authors = Author.find { Authors.name.lowerCase() inList authors }

                createdAt = Instant.fromEpochSeconds(row.getLong("created_at"))
                updatedAt = Instant.fromEpochSeconds(row.getLong("updated_at"))
            }
        }
    }

    fun migrateInventoryNumbers(db: Database) {
        val map = transaction(db) {
            SchemaUtils.create(PublicationInventoryNumbers)
            Publication.find { Publications.legacyInventoryNumber neq "" }.associate {
                it.id to it.legacyInventoryNumber
            }
        }

        val allowed = transaction(db) { Catalog.find { Catalogs.hasInventory eq true }.toList() }
        map.forEach { (id, raw) ->
            val parsed = parseRawInventoryNumber(raw)
            for ((key, set) in parsed) {
                val catalog = allowed.find { it.letter == key }
                if (catalog != null) transaction(db) {
                    set.forEach { text ->
                        PublicationInventoryNumbers.insertIgnore {
                            it[publicationId] = id
                            it[catalogId] = catalog.id
                            it[this.text] = text
                        }
                    }
                }

            }
        }
    }

    private fun parseRawYear(raw: String): LocalDate? {
        val match = yearRegexes.firstNotNullOfOrNull {
            it.matchEntire(raw)?.groups as? MatchNamedGroupCollection
        }
        if (match != null) {
            val year = match["year"]?.value?.toIntOrNull() ?: return null
            val month = kotlin.runCatching { match["month"]?.value?.toInt() }.getOrNull() ?: 1
            val day = kotlin.runCatching { match["day"]?.value?.toInt() }.getOrNull() ?: 1
            return kotlin.runCatching { LocalDate(year, month, day) }.getOrNull()
        }
        return null
    }

    private fun parseRawInventoryNumber(raw: String): Map<Char, Set<String>> {
        val parts = raw.split("#").map(String::trim).filter { it.length >= 2 }
       return parts.associate { part ->
            val key = part.first()
            val ids = part.substring(2).split(",").map(String::trim)

            key to ids.toSet()
        }
    }

    private val YearRegex1 = "(?<year>\\d{4})\\D(?<month>\\d{2})\\D(?<day>\\d{2})".toRegex()
    private val YearRegex2 = "(?<day>\\d{2})\\D(?<month>\\d{2})\\D(?<year>\\d{4})".toRegex()
    private val YearRegex3 = "^(?<year>\\d{4})$".toRegex()
    private val YearRegex4 = "^(?<year>\\d{4})".toRegex()
    private val yearRegexes = arrayOf(YearRegex1, YearRegex2, YearRegex3, YearRegex4)
}

private val PUBLICATIONS_QUERY = """
    SELECT 
    	pub.id as id, 
        pub.opublikowana = 1 as is_published,
        pub.periodyk = 1 as is_periodical,
        pub.plan = 1 as is_planned,
        pub.licencja = 1 as is_licenced,
        pub.pozycja as position,
        
        pub.tytul as title,
        pub.podtytul as subtitle,
        pub.tyt_oryg as original_title,
        pub.wspolautor as co_author,
        pub.tlumacz as translator,
        
        pub.wydanie as edition,
        pub.tom as volume,
        pub.redaktor as redactor,
        pub.kolegium as college,
        pub.ilustrator as illustrator,
        
        pub.miejsce as place,
        pub.seria as series,
        pub.rok as raw_year,
        pub.wyd_druk as publishing_house,
        pub.wydawca as publisher,
        pub.objetosc as dimensions,
        
        pub.sygnatura as signature,
        pub.isbn as isbn,
        pub.issn as issn,
        pub.ukd as ukd,
        pub.prawa as copyright,
        pub.kup_na as shop_url,
        pub.zrodlo as origin,
        
        pub.dodatek as extra,
        pub.opis as description,
        
        pub.autor as legacy_author,
        pub.lokalizacja as legacy_catalog,
        pub.nr_inw as legacy_inventory_number,
        
        pub.utw_kiedy as created_at,
        pub.red_kiedy as updated_at,
    	COALESCE(GROUP_CONCAT(p2c.id_kat), '') as category_ids
    FROM amlib_publikacje as pub
    LEFT JOIN amlib_pub2kat as p2c ON
    	pub.id = p2c.id_pub
    GROUP BY pub.id
""".trimIndent()