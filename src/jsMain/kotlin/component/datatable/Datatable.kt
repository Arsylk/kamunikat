package component.datatable

import component.datatable.body.DatatableBody
import component.datatable.body.DatatableBodyProps
import component.datatable.cell.DatatableCell
import component.datatable.footer.DatatableFooter
import component.datatable.header.DatatableHeader
import component.datatable.header.DatatableHeaderColumn
import component.datatable.toolbar.DatatableToolbar
import csstype.pct
import kotlinx.js.jso
import model.datatable.DatatableList
import mui.material.Paper
import mui.material.Size
import mui.material.Table
import mui.material.TableContainer
import react.FC
import react.Props

external interface  DatatableProps<T: DatatableItem> : Props {
    var isLoading: Boolean?
    var list: DatatableList<*, *>

    var title: String?

    var headerColumns: List<DatatableHeaderColumn>
    var sortOrder: DatatableSortOrder?

    var rowCells: List<DatatableCell<T>>

    var onAdd: (() -> Unit)?
    var onSortOrderChange: ((DatatableSortOrder?) -> Unit)?
    var onPageChange: ((Int) -> Unit)?
    var onCellClick: ((DatatableRowKey, DatatableCellKey) -> Unit)?
}
val Datatable = FC<DatatableProps<*>> { props ->
    Paper {
        sx = jso {
            width = 100.pct
        }

        DatatableToolbar {
            title = props.title
            onAdd = props.onAdd
        }

        TableContainer {
            Table {
                sx = jso {
                    size = Size.medium
                }

                DatatableHeader {
                    columns = props.headerColumns
                    sortOrder = props.sortOrder
                    onSortOrderChange = props.onSortOrderChange
                }

                @Suppress("UPPER_BOUND_VIOLATED")
                DatatableBody<DatatableBodyProps<DatatableItem>> {
                    isLoading = props.isLoading == true
                    items = props.list.items.asDynamic()
                    rowsPerPage = props.list.perPage
                    rowCells = props.rowCells.asDynamic() as List<DatatableCell<DatatableItem>>
                    onCellClick = props.onCellClick
                }
            }
        }

        DatatableFooter {
            page = props.list.page
            rowsPerPage = props.list.perPage
            fullSize = props.list.orderSelect.asDynamic()
            onPageChange = props.onPageChange
        }
    }
}