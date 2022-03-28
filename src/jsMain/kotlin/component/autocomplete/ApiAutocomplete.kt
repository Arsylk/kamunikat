package component.autocomplete

import csstype.*
import domain.base.LaunchedEffect
import domain.use
import kotlinx.js.jso
import model.user.UserTag
import mui.base.AutocompleteChangeReason
import mui.icons.material.AddCircle
import mui.material.*
import mui.material.Size
import react.*
import react.dom.html.ReactHTML.li

external interface ApiAutocompleteProps<Item: Any> : Props {
    var label: String

    var value: Set<Item>
    var onChange: (Set<Item>) -> Unit
    var fetch: suspend () -> List<Item>

    var comparator: ((Item, Item) -> Boolean)?
    var filter: (Item, String) -> Boolean
    var represent: ((Item) -> String)?

    var dialog: FC<ApiAutocompleteDialogProps>
}
val ApiAutocomplete = FC<ApiAutocompleteProps<out Any>> { rawProps ->
    val props = rawProps.unsafeCast<ApiAutocompleteProps<Any>>()

    var counter by useState(0)
    var allValues by useState(emptyArray<Any>())
//    var isOpen by useState(false)
    var isLoading by useState(true)
    LaunchedEffect(counter) {
        isLoading = true
        // TODO is error handing
        kotlin.runCatching { allValues = props.fetch().toTypedArray() }
        isLoading = false
    }

    var dialogInput by useState<String?>(null)

    val represent = { item: Any -> props.represent?.invoke(item) ?: item.toString() }
    Box {
        sx = jso {
            display = Display.flex
            flexGrow = FlexGrow(1.0)
            flexDirection = FlexDirection.row
            alignContent = AlignContent.center
            alignItems = AlignItems.center
        }

        @Suppress("UPPER_BOUND_VIOLATED")
        Autocomplete<AutocompleteProps<Any>> {
            sx = jso {
                flexGrow = FlexGrow(1.0)
            }

            options = allValues
            freeSolo = true
            multiple = true
            clearOnBlur = true
            value = props.value.toTypedArray()
            loading = isLoading
            loadingText = ReactNode("231")

//            open = isOpen
//            onOpen = { isOpen = true }
//            onClose = { _, _ -> isOpen = false }

            isOptionEqualToValue = { option, value ->
                props.comparator?.invoke(option, value) ?: (option == value)
            }
            filterOptions = { list, filter ->
                list.filter { props.filter(it, filter.inputValue) }.toTypedArray()
            }
            getOptionLabel = { item -> represent(item) }
            renderInput = { params ->
                TextField.create {
                    +params
                    label = ReactNode(props.label)
                    if (isLoading) asDynamic().InputProps = jso<InputProps> {
                        endAdornment = Fragment.create {
                            CircularProgress {
                                color = CircularProgressColor.inherit
                                size = 20
                            }
                        }
                    }
                }

            }
            renderOption = { props, item, _ ->
                li.create {
                    +props
                    +represent(item)
                }
            }
            onChange = { event, newValue, reason, details ->
                event.preventDefault()
                console.log(reason)
                console.log(newValue)
                when (reason) {
                    AutocompleteChangeReason.createOption -> {
                        val input = (newValue as? Array<String>)?.lastOrNull().orEmpty()
                        dialogInput = input
                    }
                    AutocompleteChangeReason.selectOption -> use(newValue as? Array<UserTag>) { values ->
                        props.onChange(values.toSet())
                    }
                    AutocompleteChangeReason.removeOption -> use(newValue as? Array<UserTag>) { values ->
                        props.onChange(values.toSet())
                    }
                    AutocompleteChangeReason.clear -> props.onChange(emptySet())
                    else -> {}
                }
            }
        }

        IconButton {
            sx = jso { marginLeft = 8.px }
            onClick = { dialogInput = "" }
            size = Size.large
            AddCircle()
        }
    }

    Dialog {
        open = dialogInput != null
        onClose = { _, _ -> dialogInput = null }
        props.dialog {
            input = dialogInput.orEmpty()
            dismiss = { refresh ->
                if (refresh) counter += 1
                dialogInput = null
            }
        }
    }
}

external interface ApiAutocompleteDialogProps : Props {
    var input: String
    var dismiss: (refresh: Boolean) -> Unit
}
