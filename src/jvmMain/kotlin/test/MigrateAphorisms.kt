package test

import domain.db.rawSelect
import model.db.aphorism.Aphorism
import model.db.aphorism.Aphorisms
import model.db.author.Author
import model.db.author.Authors
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

object MigrateAphorisms : IMigrate {
    /**
     * Requires:
     *  - [MigrateAuthors]
     * */
    override fun execute(db: Database, legacy: Database) {
        val list = legacy.rawSelect(APHORISM_QUERY) { row ->
            LegacyAphorism(
                title = row.getString("title").orEmpty(),
                content = row.getString("content").orEmpty(),
                rawAuthor = row.getString("raw_author") as String?,
                isPublished = row.getBoolean("is_published"),
            )
        }
        list.forEach { item ->
            transaction(db) {
                val author = if (!item.rawAuthor.isNullOrBlank()) {
                    Author.find { Authors.name.lowerCase() eq item.rawAuthor.lowercase() }
                        .firstOrNull() ?: Author.new { name = item.rawAuthor }
                } else null
                Aphorism.new {
                    title = item.title
                    content = item.content
                    isPublished = item.isPublished
                    this.author = author
                }
            }
        }
    }
}

private data class LegacyAphorism(
    val title: String,
    val content: String,
    val rawAuthor: String?,
    val isPublished: Boolean,
)
private val APHORISM_QUERY = """
    SELECT 
    	site.pagetitle as title,
        site.content as content,
        site.longtitle as raw_author,
        site.published = 1 as is_published
    FROM modx_site_content as site
    WHERE site.`description` = 'blog' AND site.parent = 8911
""".trimIndent()