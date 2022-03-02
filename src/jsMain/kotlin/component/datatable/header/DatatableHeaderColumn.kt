package component.datatable.header

import component.datatable.DatatableColumnKey
import mui.material.TableCellAlign

data class DatatableHeaderColumn(
    val key: DatatableColumnKey,
    val text: String,
    val colSpan: Int,
    val sortable: Boolean,
    val align: TableCellAlign,
)