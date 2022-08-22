package component.input

import domain.onValueChanged
import domain.use
import mui.material.TextField
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.InputType

external interface BaseNumberFieldProps : Props {
    var label: String

    var initialValue: Int?
    var value: Int?

    var onChange: (Int) -> Unit
    var required: Boolean?

    var override: BaseTextFieldProps?
}
val BaseNumberField = FC<BaseNumberFieldProps> { props ->
    TextField {
        fullWidth = true
        required = props.required == true
        label = ReactNode(props.label)
        type = InputType.number
        use(props.override) { +it }

        use(props.initialValue) { defaultValue = it }
        use(props.value) { value = it }
        onValueChanged { props.onChange(it.toIntOrNull() ?: return@onValueChanged) }
    }
}