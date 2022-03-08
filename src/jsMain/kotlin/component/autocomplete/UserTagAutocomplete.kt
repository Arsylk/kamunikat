package component.autocomplete

import csstype.*
import domain.api.ApiService
import domain.base.produceState
import domain.base.useScope
import domain.koin.get
import domain.koin.inject
import domain.onValueChanged
import domain.use
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
    val scope = useScope()

    var counter by useState(0)
    val (allTags, updateAllTags) = useState(emptyArray<UserTag>())
    useEffect(counter) {
        console.log("use eff with: $counter")
        scope.launch {
            updateAllTags(service.getUserTags().toTypedArray())
        }
    }


    val (selectedTags, updateSelectedTags) = useState<Set<UserTag>>(emptySet())
    useEffect(props.initialTags) { use(props.initialTags) { updateSelectedTags(it) } }
    var showDialog by useState(false)
    var dialogPrefill by useState("")

    Box {
        sx = jso {
            display = Display.flex
            flexGrow = FlexGrow(1.0)
            flexDirection = FlexDirection.row
            alignContent = AlignContent.center
            alignItems = AlignItems.center
        }

        @Suppress("UPPER_BOUND_VIOLATED")
        Autocomplete<AutocompleteProps<UserTag>> {
            sx = jso {
                flexGrow = FlexGrow(1.0)
            }

            options = allTags
            freeSolo = true
            multiple = true
            clearOnBlur = true
            value = selectedTags.toTypedArray()

            isOptionEqualToValue = { option, value -> option.id == value.id }
            filterOptions = { list, filter ->
                val input = filter.inputValue
                list.filter { it.name.contains(input, ignoreCase = true) }.toTypedArray()
            }
            getOptionLabel = { item -> item.name }
            renderInput = { params ->
                TextField.create {
                    +params
                    label = ReactNode("Tags")
                }
            }
            renderOption = { props, item, _ ->
                li.create {
                    +props
                    +item.name
                }
            }
            onChange = { event, newValue, reason, details ->
                event.preventDefault()
                console.log(reason)
                console.log(newValue)
                when (reason) {
                    AutocompleteChangeReason.createOption -> {
                        dialogPrefill = (newValue as? Array<String>)?.firstOrNull().orEmpty()
                        showDialog = true
                    }
                    AutocompleteChangeReason.selectOption -> use(newValue as? Array<UserTag>) { tags ->
                        updateSelectedTags { tags.toSet() }
                    }
                    AutocompleteChangeReason.removeOption -> use(newValue as? Array<UserTag>) { tags ->
                        updateSelectedTags { tags.toSet() }
                    }
                    AutocompleteChangeReason.clear -> updateSelectedTags(emptySet())
                    else -> {}
                }
            }
        }

        IconButton {
            sx = jso { marginLeft = 8.px }
            onClick = {
                dialogPrefill = ""
                showDialog = true
            }
            size = Size.large
            AddCircle()
        }
    }

    FormDialog {
        initialValue = dialogPrefill
        isOpen = showDialog
        dismiss = { showDialog = false }
        refresh = { counter++ }
    }
}

private external interface FormDialogProps : Props {
    var initialValue: String?
    var isOpen: Boolean?
    var dismiss: () -> Unit
    var refresh: () -> Unit
}
private val FormDialog = FC<FormDialogProps> { props ->
    val formId = "add-tag-form"
    val service by inject<ApiService>()
    val scope = useScope()

    var name by useState("")
    useEffect(props.initialValue) { use(props.initialValue) { name = it } }

    Dialog {
        open = props.isOpen == true
        onClose = { _, _ -> props.dismiss() }
        form {
            id = formId
            this.name = formId

            onSubmit = { e ->
                e.preventDefault()
                scope.launch {
                    service.addUserTag(UserTag(id = -1, name = name))
                    props.refresh()
                    props.dismiss()
                }
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
                    onClick = { props.dismiss() }
                }
                Button {
                    +"Add"
                    type = ButtonType.submit
                    form = formId
                }
            }
        }
    }
}
