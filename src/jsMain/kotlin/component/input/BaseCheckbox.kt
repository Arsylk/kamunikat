package component.input

import domain.use
import mui.material.Checkbox
import mui.material.FormControlLabel
import react.FC
import react.Props
import react.ReactNode
import react.create

external interface BaseCheckboxProps : Props {
    var label: String

    var initialValue: Boolean?
    var value: Boolean?

    var onChange: (Boolean) -> Unit
}
val BaseCheckbox = FC<BaseCheckboxProps> { props ->
    FormControlLabel {
        label = ReactNode(props.label)
        control = Checkbox.create {
            use(props.initialValue) { defaultChecked = it }
            use(props.value) { checked = it }
            onChange = { _, checked ->
                props.onChange(checked)
            }
        }
    }
}
