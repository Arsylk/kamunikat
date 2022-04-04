package component.autocomplete

import csstype.px
import domain.api.ApiService
import domain.base.useScope
import domain.koin.get
import domain.koin.inject
import domain.onValueChanged
import domain.update
import domain.value
import kotlinx.coroutines.launch
import kotlinx.js.jso
import model.api.catalog.Catalog
import model.user.UserTag
import mui.material.*
import mui.system.ResponsiveStyleValue
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML

external interface CatalogAutocompleteProps : Props {
    var value: Set<Catalog>
    var onChange: (Set<Catalog>) -> Unit
}
val CatalogAutocomplete = FC<CatalogAutocompleteProps> { props ->
    val service by inject<ApiService>()
    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Catalog>> {
        label = "Catalogs"
        value = props.value
        onChange = props.onChange

        fetch = { service.catalog.getList() }
        comparator = { t1, t2 -> t1.id == t2.id }
        represent = { t -> t.name }
        dialog = FormDialog
    }
}
private val FormDialog = FC<ApiAutocompleteDialogProps<Catalog>> { props ->
    val service by inject<ApiService>()
    val scope = useScope()

    val (catalog, update) = useState(Catalog(0, "", null, false))
    useEffect(props.input) { update.update { copy(name = props.input) } }

    ReactHTML.form {
        onSubmit = { e ->
            e.preventDefault()
            scope.launch {
                service.catalog.add(catalog)
                props.dismiss(true)
            }
            e.stopPropagation()
        }

        DialogTitle { +"Add Catalog" }
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
                        value = catalog.name
                        onValueChanged { update.update { copy(name = it) } }
                    }
                }
                Grid {
                    sx = jso { marginTop = 8.px }
                    item = true
                    xs = 12
                    TextField {
                        fullWidth = true
                        label = ReactNode("Letter")
                        variant = FormControlVariant.outlined
                        type = InputType.text
                        value = catalog.letter?.toString() ?: ""
                        onValueChanged { update.update { copy(letter = it.firstOrNull()) } }
                    }
                }
                Grid {
                    sx = jso { marginTop = 8.px }
                    item = true
                    xs = 12
                    FormControlLabel {
                        label = ReactNode("Has Inventory")
                        control = Checkbox.create {
                            checked = catalog.hasInventory
                            onChange = { _, checked ->
                                update.update { copy(hasInventory = checked) }
                            }
                        }
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
