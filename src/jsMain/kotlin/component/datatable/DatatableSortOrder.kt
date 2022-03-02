package component.datatable

import model.common.Order


data class DatatableSortOrder(
    val key: DatatableColumnKey,
    val order: Order,
)