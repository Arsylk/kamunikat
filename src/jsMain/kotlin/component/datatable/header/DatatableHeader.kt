package component.datatable.header

import domain.onClickEvent
import model.common.Order
import model.common.cycle
import mui.material.*
import react.FC
import react.Props
import react.PropsWithChildren


external interface DatatableHeaderProps<Field : Enum<Field>> : PropsWithChildren {
    var order: Order?
    var orderSelect: Field?
    var onOrderChange: ((Order?, Field?) -> Unit)?
}

external interface DatatableHeaderCellProps : Props {
    var text: String
    var sortable: Boolean?
    var order: Order?
    var active: Boolean?
    var onClickEvent: (() -> Unit)?

    var colSpan: Int?
    var align: TableCellAlign?
}
val DatatableHeaderCell = FC<DatatableHeaderCellProps> { props ->
    TableCell {
        colSpan = props.colSpan
        align = props.align

        if (props.sortable == true) TableSortLabel {
            active = props.active == true
            direction = when (props.order) {
                Order.Descending -> TableSortLabelDirection.desc
                else -> TableSortLabelDirection.asc
            }
            +props.text
        }
        else +props.text

        onClickEvent { props.onClickEvent?.invoke() }
    }
}