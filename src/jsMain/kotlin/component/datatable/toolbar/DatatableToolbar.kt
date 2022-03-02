package component.datatable.toolbar

import csstype.FlexGrow
import domain.onClickEvent
import kotlinx.js.jso
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Toolbar
import mui.material.Typography
import react.FC
import react.Props
import react.dom.html.ReactHTML


external interface DatatableToolbarProps : Props {
    var title: String?
    var onAdd: (() -> Unit)?
}
val DatatableToolbar = FC<DatatableToolbarProps> { props ->
    Toolbar {
        Typography {
            variant = "h6"
            component = ReactHTML.div
            sx = jso { flexGrow = FlexGrow(1.0) }
            +props.title.orEmpty()
        }
        if (props.onAdd != null) Button {
            variant = ButtonVariant.outlined
            +"Add"
            onClickEvent { props.onAdd?.invoke() }
        }
    }
}