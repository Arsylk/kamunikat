package test

import model.db.catalog.Catalog
import model.db.catalog.Catalogs
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

object MigrateCatalogs : IMigrate {
    override fun execute(db: Database, legacy: Database) {
        //first(db, legacy)
        //pubIdToCatalogIds(db, legacy)
    }


    private fun first(db: Database, legacy: Database) {
        CATALOG_NAMES.forEach { name ->
            db.sequenceOf(Catalogs).add(
                Catalog {
                    this.name = name
                }
            )
        }
    }

    private fun pubIdToCatalogIds(db: Database, legacy: Database) {
        val catalogs = db.sequenceOf(Catalogs).toList()
        val map = legacy.useConnection { conn ->
            conn.prepareStatement(CATALOGS_QUERY).executeQuery()
                .asIterable().associate {
                    val id = it.getInt("id")
                    val raw = it.getString("raw_catalog") as String

                    id to catalogs.filter { c -> raw.contains(c.name, ignoreCase = true) }
                }
        }
    }

    private fun searchRaw(db: Database, legacy: Database) {
        val list = legacy.useConnection { conn ->
            conn.prepareStatement(CATALOGS_QUERY).executeQuery()
                .asIterable().map { it.getString("name") }
                .flatMap { it.split(",") }
                .map { it.trim() }
        }.toSet()
        println(list)
    }

}

private val CATALOGS_QUERY = """
    SELECT id, lokalizacja as raw_catalog FROM amlib_publikacje 
    WHERE lokalizacja IS NOT NULL AND lokalizacja <> ""
""".trimIndent()
private val CATALOG_NAMES = setOf(
    "KAMUNIKAT", "JAREK", "MiOKB", "EEDC", "BTH", "SKARYNA"
)