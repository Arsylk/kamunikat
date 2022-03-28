@file:JvmName("SortSelectableJvmKt")
package model.common

import org.jetbrains.exposed.sql.Column


interface DbSortSelectable<T: Enum<T>> : ISortSelectable<T> {
    override fun select(field: T): Column<*>
}
