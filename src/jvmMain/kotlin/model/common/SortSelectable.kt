@file:JvmName("SortSelectableJvmKt")
package model.common

import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sortedBy
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column


interface DbSortSelectable<T: Enum<T>> : ISortSelectable<T> {
    override fun select(field: T): Column<*>
}

fun <S: Enum<S>, E: Any, T> EntitySequence<E, T>.sortedBy(
    field: S?,
    order: Order?,
): EntitySequence<E, T> where T: BaseTable<E>, T : DbSortSelectable<S> {
    field ?: return this
    val seq = sortedBy {
        val column = it.select(field)
        when (order) {
            Order.Ascending -> column.asc()
            Order.Descending -> column.desc()
            else -> column.asc()
        }
    }
    return seq
}
