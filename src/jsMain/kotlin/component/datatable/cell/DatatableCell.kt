package component.datatable.cell

import component.datatable.DatatableCellKey
import component.datatable.DatatableItem
import domain.onClickEvent
import mui.material.TableCell
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface DatatableCellProps<T: DatatableItem> : Props {
    var item: T
    var currentPosition: Int

    var onCellClick: (() -> Unit)?
}

external interface SimpleDatatableCellProps : Props {
    var text: String
    var onClick: (() -> Unit)?
}
val SimpleDatatableCell = FC<SimpleDatatableCellProps> { props ->
    TableCell {
        component = ReactHTML.th
        scope = "row"
        +props.text
        onClickEvent { props.onClick?.invoke() }
    }
}

data class DatatableCell<T: DatatableItem>(
    val key: DatatableCellKey,
    val fc: FC<DatatableCellProps<T>>
)