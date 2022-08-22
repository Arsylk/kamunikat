package component.input

import domain.onValueChanged
import domain.use
import kotlinx.js.jso
import mui.material.InputBaseComponentProps
import mui.material.TextField
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.InputType

external interface BaseTextFieldProps : Props {
    var label: String

    var initialValue: String?
    var value: String?

    var onChange: (String) -> Unit
    var required: Boolean?
    var limit: Int?

    var override: mui.material.BaseTextFieldProps?
}
val BaseTextField = FC<BaseTextFieldProps> { props ->
    TextField {
        fullWidth = true
        required = props.required == true
        label = ReactNode(props.label)
        type = InputType.text
        use(props.limit) { limit ->
            inputProps = jso<dynamic> { maxLength = limit }.unsafeCast<InputBaseComponentProps>()
        }

        use(props.override) { +it }
        use(props.initialValue) { defaultValue = it }
        use(props.value) { value = it }
        onValueChanged(props.onChange)
    }
}