package component.input

import domain.use
import mui.material.TextField
import org.w3c.dom.HTMLTextAreaElement
import react.FC
import react.Props
import react.ReactNode

external interface BaseMultilineFieldProps : Props {
    var label: String

    var initialValue: String?
    var value: String?

    var onChange: (String) -> Unit
    var required: Boolean?

    var override: BaseTextFieldProps?
}
val BaseMultilineField = FC<BaseMultilineFieldProps> { props ->
    TextField {
        fullWidth = true
        required = props.required == true
        label = ReactNode(props.label)
        multiline = true
        use(props.override) { +it }

        use(props.initialValue) { defaultValue = it }
        use(props.value) { value = it }
        onInput = {
            val t = it.target
            if (t is HTMLTextAreaElement) props.onChange(t.value)
        }
    }
}