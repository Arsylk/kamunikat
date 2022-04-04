package component.autocomplete

import csstype.*
import domain.base.LaunchedEffect
import domain.base.produceState
import domain.use
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.js.jso
import model.user.UserTag
import mui.base.AutocompleteChangeReason
import mui.base.AutocompleteInputChangeReason
import mui.icons.material.AddCircle
import mui.material.*
import mui.material.Size
import mui.system.sx
import npm.react.window.VariableSizeList
import npm.react.window.VariableSizeListClass
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import kotlin.time.DurationUnit


private fun useResetCache(data: Any): RefObject<VariableSizeListClass<*>> {
    val ref = useRef<VariableSizeListClass<*>>(null)
    useEffect(data) {
        use(ref.current) { it.resetAfterIndex(0, true) }
    }
    return ref
}
private val OuterElementContext = createContext(jso<Props>())
private val OuterElementType = rawForwardRef<HTMLDivElement, PropsWithRef<HTMLDivElement>> { props, ref ->
    val outerProps = useContext(OuterElementContext)
    div.create {
        this.ref = ref
        +props
        +outerProps
    }
}


@Suppress("UPPER_BOUND_VIOLATED")
private val ListBoxComponent = rawForwardRef<HTMLDivElement, HTMLAttributes<HTMLElement>> { props, ref ->
    val children = props.children.unsafeCast<Array<ReactNode>>()
    val itemData = children.flatMap { item ->
        listOf(item) + (item.asDynamic().children as? Array<ReactNode>).orEmpty()
    }

    val itemCount = itemData.size
    val itemSize = 48
    val getChildSize = { child: ReactNode -> itemSize }
    val getHeight = {
        if (itemCount > 8) 8 * itemSize
        else itemData.map(getChildSize).sum()
    }

    val gridRef = useResetCache(itemCount)

    div.create {
        this.ref = ref
        OuterElementContext.Provider {
            value = props

            @Suppress("UPPER_BOUND_VIOLATED")
            VariableSizeList {
                this.itemData = itemData
                height = number((getHeight() + 12.0))
                width = 100.pct
                this.ref = gridRef
                outerElementType = OuterElementType
                innerElementType = "ul"
                this.itemSize = { i -> number(getChildSize(itemData[i]).toDouble()) }
                overscanCount = 5
                this.itemCount = itemCount
                this.children = "li".unsafeCast<ReactNode>()
            }
        }
    }
}

external interface ApiAutocompleteProps<Item: Any> : Props {
    var label: String

    var value: Set<Item>
    var onChange: (Set<Item>) -> Unit
    var fetch: suspend () -> List<Item>

    var comparator: ((Item, Item) -> Boolean)?
    var represent: ((Item) -> String)?

    var dialog: FC<ApiAutocompleteDialogProps<Item>>
}
val ApiAutocomplete = FC<ApiAutocompleteProps<out Any>> { rawProps ->
    val props = rawProps.unsafeCast<ApiAutocompleteProps<Any>>()

    var counter by useState(0)
    var allValues by useState(emptyArray<Any>())
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
        sx {
            display = Display.flex
            flexGrow = number(1.0)
            flexDirection = FlexDirection.row
            alignContent = AlignContent.center
            alignItems = AlignItems.center
        }

        @Suppress("UPPER_BOUND_VIOLATED")
        Autocomplete<AutocompleteProps<Any>> {
            sx { flexGrow = number(1.0) }

            options = allValues
            value = props.value.toTypedArray()

            freeSolo = true
            multiple = true
            clearOnBlur = true
            loading = isLoading
            loadingText = ReactNode("Loading...")
            noOptionsText = ReactNode("Nothing here")

            isOptionEqualToValue = { option, value ->
                props.comparator?.invoke(option, value) ?: (option == value)
            }
            getOptionLabel = { item -> represent(item) }
            renderInput = { params ->
                TextField.create {
                    +params
                    label = ReactNode(props.label)
                }
            }
            onChange = { event, newValue, reason, _ ->
                event.preventDefault()
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

            disableListWrap = true
            ListboxComponent = ListBoxComponent
        }


        IconButton {
            sx = jso { marginLeft = 8.px }
            onClick = { dialogInput = "" }
            size = Size.large
            if (isLoading) CircularProgress {
                color = CircularProgressColor.inherit
                size = 20
            } else AddCircle()
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
            add = { item -> props.onChange(props.value + item) }
        }
    }
}

external interface ApiAutocompleteDialogProps<Item: Any> : Props {
    var input: String
    var dismiss: (refresh: Boolean) -> Unit
    var add: (item: Item) -> Unit
}
