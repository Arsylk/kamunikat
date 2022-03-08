package component.datatable.body

import component.datatable.DatatableRowKey
import component.datatable.row.DatatableRowProps
import csstype.*
import kotlinx.js.jso
import mui.material.*
import mui.material.styles.Theme
import mui.material.styles.useTheme
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.key

external interface DatatableBodyProps<T : Any> : Props {
    var isLoading: Boolean?
    var items: List<T>
    var rowsPerPage: Int
    var keySelector: (T) -> Any

    var RenderRow: FC<DatatableRowProps<T>>
}

val DatatableBody = FC<DatatableBodyProps<*>> { _props ->
    val props = _props.unsafeCast<DatatableBodyProps<Any>>()
    val emptyRows = props.rowsPerPage - props.items.size

    TableBody {
        props.items.forEachIndexed { i, item ->
            val rowKey = DatatableRowKey(props.keySelector(item))
            TableRow {
                key = rowKey.toString()
                hover = true

                props.RenderRow {
                    this.item = item
                    position = i
                }
            }
        }

        for (i in 0 until emptyRows) {
            TableRow {
                TableCell {
                    component = ReactHTML.th
                    scope = "row"
                    colSpan = 9999
                }
            }
        }
    }
}