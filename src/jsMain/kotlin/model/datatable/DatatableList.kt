package model.datatable

import model.common.Order
import model.common.SortSelectable

data class DatatableList<T: Enum<T>, R: SortSelectable<T>>(
    val page: Int,
    val perPage: Int,
    val order: Order?,
    val orderSelect: T?,
    val fullSize: Int,
    val items: List<R>,
)