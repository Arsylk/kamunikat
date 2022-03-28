package test

import domain.db.rawExec
import model.db.catalog.Catalog
import model.db.catalog.Catalogs
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object MigrateCatalogs : IMigrate {
    override fun execute(db: Database, legacy: Database) {
        insertCatalogs(db)
    }


    private fun insertCatalogs(db: Database) {
        transaction(db) {
            CATALOGS.forEach { item ->
                Catalog.new {
                    name = item.name
                    letter = item.letter
                    hasInventory = item.hasInventory
                }
            }
        }
    }

    private fun searchRaw(legacy: Database) {
        val list = legacy.rawExec(CATALOGS_QUERY) { row ->
            row.getString("name").split(",").map(String::trim)
        }.flatten().toSet()
        println(list)
    }

}

private data class BaseCatalog(val name: String, val letter: Char?, val hasInventory: Boolean)

private val CATALOGS_QUERY = """
    SELECT id, lokalizacja as raw_catalog FROM amlib_publikacje 
    WHERE lokalizacja IS NOT NULL AND lokalizacja <> ""
""".trimIndent()
private val CATALOGS = listOf(
    BaseCatalog("KAMUNIKAT", null, false),
    BaseCatalog("JAREK", null, false),
    BaseCatalog("MiOKB", 'M', true),
    BaseCatalog("EEDC", 'E', true),
    BaseCatalog("BTH", 'B', true),
    BaseCatalog("SKARYNA", null, false),
)