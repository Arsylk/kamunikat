package test

import model.db.category.Categories
import model.db.category.Category
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf

object MigrateCategories : IMigrate {
    override fun execute(db: Database, legacy: Database) {
        first(db, legacy)
    }

    private fun first(db: Database, legacy: Database) {
        val map = legacy.useConnection { conn ->
            conn.prepareStatement(CATEGORY_QUERY).executeQuery()
                .asIterable().associate {
                    it.getInt("id") to it.getString("nazwa") as String
                }
        }
        map.forEach { entry ->
            db.sequenceOf(Categories).add(
                Category {
                    id = entry.key
                    name = entry.value
                }
            )
        }
    }
}

private val CATEGORY_QUERY = """
    SELECT id, nazwa FROM `amlib_kategorie`
""".trimIndent()