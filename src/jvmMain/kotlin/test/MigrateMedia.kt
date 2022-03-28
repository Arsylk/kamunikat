package test

import domain.db.rawSelect
import model.db.publication.PublicationAttachments
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object MigrateMedia : IMigrate {
    /**
     * Requires:
     *  - [MigratePublications]
     * */
    override fun execute(db: Database, legacy: Database) {
        val list = legacy.rawSelect(MEDIA_QUERY) { row ->
            LegacyMedia(
                id = row.getInt("id"),
                publicationId = row.getInt("publication_id"),
                parentId = row.getString("parent_id")?.toIntOrNull(),
                title = row.getString("title").orEmpty(),
                fileType = row.getString("file_type") as String?,
                fileName = row.getString("file_name") as String?,
                position = row.getInt("position"),
            )
        }
        transaction(db) {
            list.forEach { item ->
                PublicationAttachments.insert {
                    it[id] = item.id
                    it[publicationId] = item.publicationId
                    item.parentId?.apply { it[parentId] = this }
                    it[title] = item.title
                    it[fileType] = item.fileType
                    it[fileName] = item.fileName
                    it[position] = item.position
                }
            }
        }
    }
}

private data class LegacyMedia(
    val id: Int,
    val publicationId: Int,
    val parentId: Int?,
    val title: String,
    val fileType: String?,
    val fileName: String?,
    val position: Int,
)

private val MEDIA_QUERY = """
    WITH dataset AS (
    	SELECT mi.* FROM media as m
    	JOIN amlib_publikacje as pub ON pub.id = m.pub_id 
    	JOIN media as mi ON m.id = mi.rodzic
        UNION
        SELECT m.* FROM media as m
    	JOIN amlib_publikacje as pub ON pub.id = m.pub_id 
        WHERE m.rodzic = -1
    )
    SELECT 
        dataset.id as id,
        if(dataset.rodzic <> -1, dataset.rodzic, null) as parent_id,
    	dataset.pub_id as publication_id,
        dataset.tytul as title,
        if(dataset.file_typ <> '0', dataset.file_typ, null) as file_type, 
        if(dataset.file_typ <> '0', concat(dataset.pub_id, '-', dataset.chap_id, '.', dataset.file_typ), null) as file_name,
        dataset.poz as position
    FROM dataset;
""".trimIndent()