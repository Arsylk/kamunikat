package model.datatable

import model.common.Order
import model.common.Sortable

data class DatatableList<T: Enum<T>, R: Sortable<T>>(
    val page: Int,
    val perPage: Int,
    val order: Order?,
    val orderSelect: T?,
    val fullSize: Int,
    val items: List<R>,
)