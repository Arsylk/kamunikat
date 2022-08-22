package model.cms

import domain.db.idInt
import model.db.periodical.Periodical
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import model.api.periodical.Periodical as CommonPeriodical
import model.db.periodical.toCommon as toCommonExt

object PeriodicalCmsPattern : DbCmsPattern<Periodical, CommonPeriodical>() {
    override val intEntity = Periodical

    override fun Periodical.intoEntity(item: CommonPeriodical, isNew: Boolean) {
        title = item.title
        content = item.content
        isPublished = item.isPublished
    }

    override fun Periodical.toCommon() = toCommonExt()

    suspend fun getSimpleList(): List<CommonPeriodical> {
        return newSuspendedTransaction(db = db) {
            intEntity.listQuery().map {
                CommonPeriodical(
                    id = it.idInt,
                    title = it.title,
                    content = null,
                    isPublished = it.isPublished,
                )
            }
        }
    }
}