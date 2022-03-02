package domain.db.legacy

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object AmlibPublications : Table<AmlibPublication>("amlib_publikacje") {
    val id = int("id").primaryKey().bindTo { it.id }
    val inventoryNumber = varchar("nr_inw").bindTo { it.inventoryNumber }
    val author = varchar("autor").bindTo { it.author }
}

interface AmlibPublication : Entity<AmlibPublication> {
    val id: Int
    val inventoryNumber: String
    val author: String
}