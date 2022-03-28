package model.db.publication

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PublicationAttachments : IntIdTable("publication_attachment") {
    val publicationId = reference("publication_id", Publications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    val parentId = optReference("parent_id", PublicationAttachments,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.CASCADE,
    )
    val title = varchar("title", length = 511)
    val fileType = varchar("file_type", length = 15).nullable()
    val fileName = varchar("file_name", length = 31).nullable()
    val position = integer("position").default(0)
}

class PublicationAttachment(id: EntityID<Int>) : IntEntity(id) {
    var publication by Publication referencedOn PublicationAttachments.publicationId
    var parent by PublicationAttachment optionalReferencedOn PublicationAttachments.parentId
    var title by PublicationAttachments.title
    var fileType by PublicationAttachments.fileType
    var fileName by PublicationAttachments.fileName
    var position by PublicationAttachments.position

    companion object : IntEntityClass<PublicationAttachment>(PublicationAttachments)
}