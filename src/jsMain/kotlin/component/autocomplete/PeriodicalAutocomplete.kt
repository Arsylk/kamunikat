package component.autocomplete

import component.input.BaseCheckbox
import component.input.BaseMultilineField
import component.input.BaseTextField
import csstype.px
import domain.api.ApiService
import domain.base.useScope
import domain.koin.inject
import domain.update
import kotlinx.coroutines.launch
import kotlinx.js.jso
import model.api.periodical.Periodical
import mui.material.*
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML
import react.useEffect
import react.useState

external interface PeriodicalAutocompleteProps : Props {
    var value: Set<Periodical>
    var onChange: (Set<Periodical>) -> Unit
}
val PeriodicalAutocomplete = FC<PeriodicalAutocompleteProps> { props ->
    val service by inject<ApiService>()

    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Periodical>> {
        label = "Periodical"
        value = props.value
        onChange = props.onChange

        fetch = { service.periodical.getSimpleList() }
        comparator = { t1, t2 -> t1.id == t2.id }
        represent = { t -> t.title }
        dialog = FormDialog
    }
}
private val FormDialog = FC<ApiAutocompleteDialogProps<Periodical>> { props ->
    val service by inject<ApiService>()
    val scope = useScope()

    val (periodical, update) = useState(
        Periodical(title = "", content = null, isPublished = false)
    )
    useEffect(props.input) { update.update { copy(title = props.input) } }

    ReactHTML.form {
        onSubmit = { e ->
            e.preventDefault()
            scope.launch {
                val new = service.periodical.add(periodical)
                props.dismiss(true)
                props.add(new)
            }
            e.stopPropagation()
        }

        DialogTitle { +"Add Periodical" }
        DialogContent {
            Grid {
                container = true
                spacing = ResponsiveStyleValue(2)
                direction = ResponsiveStyleValue(GridDirection.column)

                Grid {
                    sx = jso { marginTop = 8.px }
                    item = true
                    xs = 12
                    BaseTextField {
                        label = "Title"
                        value = periodical.title
                        onChange = { update.update { copy(title = it) } }
                    }
                }
                Grid {
                    item = true
                    xs = 12
                    BaseMultilineField {
                        label = "Content"
                        value = periodical.content.orEmpty()
                        onChange = { update.update { copy(content = it) } }
                    }
                }
                Grid {
                    item = true
                    xs = 3
                    BaseCheckbox {
                        label = "Is Published"
                        value = periodical.isPublished
                        onChange = { update.update { copy(isPublished = it) } }
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
