package component.autocomplete

import csstype.px
import domain.api.ApiService
import domain.base.useScope
import domain.koin.inject
import domain.onValueChanged
import domain.update
import kotlinx.coroutines.launch
import kotlinx.js.jso
import model.api.catalog.Catalog
import model.api.category.Category
import model.common.intIdComparator
import mui.material.*
import mui.system.ResponsiveStyleValue
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML

external interface CategoryAutocompleteProps : Props {
    var value: Set<Category>
    var onChange: (Set<Category>) -> Unit
}
val CategoryAutocomplete = FC<CategoryAutocompleteProps> { props ->
    val service by inject<ApiService>()
    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Category>> {
        label = "Categories"
        value = props.value
        onChange = props.onChange

        fetch = { service.category.getList() }
        comparator = ::intIdComparator
        represent = { t -> t.name }
        dialog = FormDialog
    }
}
private val FormDialog = FC<ApiAutocompleteDialogProps<Category>> { props ->
    val service by inject<ApiService>()
    val scope = useScope()

    val (category, update) = useState(Category(0, ""))
    useEffect(props.input) { update.update { copy(name = props.input) } }

    ReactHTML.form {
        onSubmit = { e ->
            e.preventDefault()
            scope.launch {
                val new = service.category.add(category)
                props.add(new)
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
                        value = category.name
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

