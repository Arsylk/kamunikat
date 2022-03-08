package component.datatable.cell

import domain.onClickEvent
import mui.material.TableCell
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface DatatableCellProps<Item: Any> : Props {
    var item: Item
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