package component.datatable.header

import component.datatable.DatatableSortOrder
import domain.onClickEvent
import model.common.Order
import model.common.cycle
import mui.material.*
import react.FC
import react.Props


external interface DatatableHeaderProps : Props {
    var columns: List<DatatableHeaderColumn>
    var sortOrder: DatatableSortOrder?
    var onSortOrderChange: ((DatatableSortOrder?) -> Unit)?
}
val DatatableHeader = FC<DatatableHeaderProps> { props ->
    TableHead {
        TableRow {
            props.columns.forEach { column ->
                val currentSort = props.sortOrder.takeIf { it?.key == column.key }
                TableCell {
                    colSpan = column.colSpan
                    align = column.align
                    TableSortLabel {
                        active = false
                        active = currentSort != null
                        direction = when (currentSort?.order) {
                            Order.Descending -> TableSortLabelDirection.desc
                            else -> TableSortLabelDirection.asc
                        }
                        +column.text

                        onClickEvent {
                            if (column.sortable) {
                                val newOrder = currentSort?.order.cycle()
                                    ?.let { DatatableSortOrder(column.key, it) }
                                props.onSortOrderChange?.invoke(newOrder)
                            }
                        }
                    }
                }
            }
        }
    }
}