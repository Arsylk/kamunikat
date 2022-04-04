package model.cms

import domain.cms.CmsPattern
import domain.db.idInt
import model.api.SuccessResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbCmsPattern<Entity: IntEntity, Common: Any> : CmsPattern<Common>, KoinComponent {
    val db by inject<Database>()
    abstract val intEntity: IntEntityClass<Entity>

    abstract fun Entity.intoEntity(item: Common)

    abstract fun Entity.toCommon(): Common


    open fun IntEntityClass<Entity>.getQuery(id: Int): Entity = get(id)

    open fun IntEntityClass<Entity>.listQuery(): Iterable<Entity> = all()


    override suspend fun getList(): List<Common> {
        return newSuspendedTransaction(db = db) {
            intEntity.listQuery().map { it.toCommon() }
        }
    }

    override suspend fun get(id: Int): Common {
        return newSuspendedTransaction(db = db) {
            intEntity.getQuery(id).toCommon()
        }
    }

    override suspend fun add(item: Common): Common {
        val newItem = newSuspendedTransaction(db = db) {
            intEntity.new { intoEntity(item) }
        }
        return newSuspendedTransaction(db = db) {
            intEntity.getQuery(newItem.idInt).toCommon()
        }
    }

    override suspend fun update(id: Int, item: Common): SuccessResponse {
        val result = kotlin.runCatching {
            newSuspendedTransaction(db = db) {
                intEntity.getQuery(id).apply { intoEntity(item) }
            }
        }
        return SuccessResponse(result)
    }

    override suspend fun delete(id: Int): SuccessResponse {
        val result = kotlin.runCatching {
            newSuspendedTransaction(db = db) { intEntity.getQuery(id).delete() }
        }
        return SuccessResponse(result)
    }
}