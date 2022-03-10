package component.autocomplete

import csstype.*
import domain.api.ApiService
import domain.base.LaunchedEffect
import domain.base.produceState
import domain.base.useScope
import domain.koin.get
import domain.koin.inject
import domain.onValueChanged
import domain.use
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import model.user.UserTag
import mui.base.AutocompleteChangeReason
import mui.icons.material.AddCircle
import mui.material.*
import mui.material.Size
import mui.system.ResponsiveStyleValue
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li

external interface UserTagAutocompleteProps : Props {
    var initialTags: Set<UserTag>?
    var onTagChange: (Set<UserTag>) -> Unit
}
val UserTagAutocomplete = FC<UserTagAutocompleteProps> { props ->
    val service = get<ApiService>()
    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<UserTag>> {
        label = "Tags"
        initialValues = props.initialTags

        fetch = { service.getUserTags() }
        comparator = { t1, t2 -> t1.id == t2.id }
        filter = { t, s -> t.name.contains(s, ignoreCase = true) }
        represent = { t -> t.name }

        dialog = FC { props ->
            FormDialog {
                +props
            }
        }
    }
}

private external interface FormDialogProps : ApiAutocompleteDialogProps {
}
private val FormDialog = FC<FormDialogProps> { props ->
    val service by inject<ApiService>()
    val scope = useScope()

    var name by useState("")
    useEffect(props.input) { name = props.input }

    form {
        onSubmit = { e ->
            e.preventDefault()
            scope.launch {
                service.addUserTag(UserTag(id = -1, name = name))
                props.dismiss(true)
            }
            e.stopPropagation()
        }

        DialogTitle { +"Add tag" }
        DialogContent {
            Grid {
                container = true
                spacing = ResponsiveStyleValue(2)
                direction = ResponsiveStyleValue(GridDirection.column)

                Grid {
                    sx = jso { marginTop = 8.px }
                    item = true
                    xs = 12
                    TextField {
                        fullWidth = true
                        label = ReactNode("Name")
                        variant = FormControlVariant.outlined
                        type = InputType.text
                        value = name
                        onValueChanged { name = it }
                    }
                }
            }
        }
        DialogActions {
            Button {
                +"Cancel"
                onClick = { props.dismiss(false) }
            }
            Button {
                +"Add"
                type = ButtonType.submit
            }
        }
    }
}
