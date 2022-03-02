package component.datatable.body

import component.datatable.DatatableCellKey
import component.datatable.DatatableItem
import component.datatable.DatatableRowKey
import component.datatable.cell.DatatableCell
import mui.material.Autocomplete
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableRow
import react.FC
import react.Props
import react.key

external interface DatatableBodyProps<T: DatatableItem> : Props {
    var items: List<T>
    var rowsPerPage: Int
    var isLoading: Boolean?

    var rowCells: List<DatatableCell<T>>
    var onCellClick: ((DatatableRowKey, DatatableCellKey) -> Unit)?
}
val DatatableBody = FC<DatatableBodyProps<*>> { props ->
    val emptyRows = props.rowsPerPage - props.items.size
    TableBody {
        props.items.forEachIndexed { i, item ->
            val rowKey = DatatableRowKey(item.key)
            TableRow {
                key = rowKey.toString()
                hover = true

                props.rowCells.forEach { rawRowCell ->
                    val rowCell = rawRowCell.asDynamic() as DatatableCell<DatatableItem>
                    rowCell.fc {
                        this.item = item
                        currentPosition = i
                        onCellClick = {
                            props.onCellClick?.invoke(rowKey, rowCell.key)
                        }
                    }
                }
            }
        }

        for(i in 0 until emptyRows) {
            TableRow {}
        }
    }
}