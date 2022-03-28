package test

import domain.db.idInt
import domain.db.new
import domain.db.rawSelect
import model.db.periodical.Periodical
import model.db.periodical.Periodicals
import model.db.publication.Publication
import model.db.publication.PublicationPeriodicalXrefs
import model.db.publication.Publications
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.transactions.transaction

object MigratePeriodicals : IMigrate {
    /**
     * Requires:
     *  - [MigratePublications]
     * */
    override fun execute(db: Database, legacy: Database) {
        val list = legacy.rawSelect(PERIODICALS_QUERY) { row ->
            LegacyPeriodical(
                id = row.getInt("id"),
                title = row.getString("title").orEmpty(),
                content = null,
                isPublished = row.getBoolean("is_published"),
                matchList = parseContentList(row.getString("list_content")),
            )
        }
        transaction(db) {
            list.forEach { item ->
                val periodical = Periodical.new {
                    title = item.title
                    content = null
                    isPublished = item.isPublished
                }
                val publicationIds = Publication.find {
                    Publications.title.lowerCase() inList item.matchList.map { it.lowercase() }
                }.map { it.id }.toSet()

                publicationIds.forEach { pubId ->
                    PublicationPeriodicalXrefs.insertIgnore {
                        it[publicationId] = pubId
                        it[periodicalId] = periodical.id
                    }
                }
            }
        }
    }

    private fun parseContentList(raw: String): List<String> {
        return ListContentRegex.findAll(raw).toList()
            .mapNotNull { it.groupValues.getOrNull(1) }
    }

    private val ListContentRegex = "'(.+?)'".toRegex()
}

private data class LegacyPeriodical(
    val id: Int,
    val title: String,
    val content: String?,
    val isPublished: Boolean,
    val matchList: List<String>,
)

private val PERIODICALS_QUERY = """
    SELECT 
	MAX(content.id) as id, 
	MAX(content.pagetitle) as title, 
	MAX(content.longtitle) as raw_alias, 
    MAX(content.content) as raw_content,
        IF(
            MAX(content.content) REGEXP '&item=`.+`', 
            REGEXP_REPLACE(MAX(content.content), '[^`]*&item=`\'?(.+?)\'?`[^`]*', '\'${'$'}1\'', 1, 0, 'm'), 
            ''
        ) as list_content,
        MAX(content.published) = 1 as is_published
    FROM 
        modx_site_content as content
    WHERE 
        content.parent = 4 AND
        content.`description` = 'magazines_category'
    GROUP BY content.pagetitle 
    ORDER BY MAX(content.id);
""".trimIndent()