@file:JvmName("SortSelectableJvmKt")
package model.common

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SizedIterable


interface DbSortable<T: Enum<T>> : ISortable<T> {
    override fun select(field: T): Column<*>
}

fun <T: Enum<T>, R: DbSortable<T>> SizedIterable<R>.sortedBy(
    field: T,
    order: Order = Order.Ascending,
) {
    orderBy(sele)
}