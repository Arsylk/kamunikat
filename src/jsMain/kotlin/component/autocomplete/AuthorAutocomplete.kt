package component.autocomplete

import csstype.px
import domain.api.ApiService
import domain.base.useScope
import domain.koin.inject
import domain.onValueChanged
import domain.update
import kotlinx.coroutines.launch
import kotlinx.js.jso
import model.api.author.Author
import model.api.catalog.Catalog
import mui.material.*
import mui.system.ResponsiveStyleValue
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML

external interface AuthorAutocompleteProps : Props {
    var value: Set<Author>
    var onChange: (Set<Author>) -> Unit
}
val AuthorAutocomplete = FC<AuthorAutocompleteProps> { props ->
    val service by inject<ApiService>()
    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Author>> {
        label = "Catalogs"
        value = props.value
        onChange = props.onChange

        fetch = { service.author.getList() }
        comparator = { t1, t2 -> t1.id == t2.id }
        filter = { t, s ->
            (setOf(t.name) + t.aliases.map { it.name }.toSet())
                .any { it.contains(s, ignoreCase = true) }
        }
        represent = { t -> if (t.hasInfo) "[${t.name}]" else t.name }
        dialog = FormDialog
    }
}

private val FormDialog = FC<ApiAutocompleteDialogProps> { props ->
    val service by inject<ApiService>()
    val scope = useScope()

    val (author, update) = useState(Author(0, "", emptyList(), false))
    useEffect(props.input) { update.update { copy(name = props.input) } }

    ReactHTML.form {
        onSubmit = { e ->
            e.preventDefault()
            scope.launch {
                service.author.add(author)
                props.dismiss(true)
            }
            e.stopPropagation()
        }

        DialogTitle { +"Add Author" }
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
                        value = author.name
                        onValueChanged { update.update { copy(name = it) } }
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
