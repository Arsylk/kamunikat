package test

import domain.db.rawExec
import model.db.category.Category
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object MigrateCategories : IMigrate {
    override fun execute(db: Database, legacy: Database) {
        insertCategories(db, legacy)
    }

    private fun insertCategories(db: Database, legacy: Database) {
        val map = legacy.rawExec(CATEGORY_QUERY) { row ->
            row.getInt("id") to row.getString("nazwa") as String
        }.toMap()

        transaction(db) {
            map.forEach { (id, name) ->
                Category.new(id = id) {
                    this.name = name
                }
            }
        }
    }
}

private val CATEGORY_QUERY = """
    SELECT id, nazwa FROM `amlib_kategorie`
""".trimIndent()