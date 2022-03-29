@file:JvmName("SortableJvmKt")
package model.common

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder


interface DbSortable<T: Enum<T>> : ISortable<T> {
    override fun select(field: T): Column<*>
}

fun <T: Enum<T>, R: DbSortable<T>> R.sortedBy(
    field: T,
    order: Order = Order.Ascending,
): Pair<Expression<*>, SortOrder> {
    return select(field) to when (order) {
        Order.Ascending -> SortOrder.ASC
        Order.Descending -> SortOrder.DESC
    }
}