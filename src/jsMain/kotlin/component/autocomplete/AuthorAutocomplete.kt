package component.autocomplete

import csstype.number
import csstype.pct
import csstype.px
import domain.api.ApiService
import domain.base.LaunchedEffect
import domain.base.useScope
import domain.koin.inject
import domain.onValueChanged
import domain.update
import domain.use
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.js.jso
import model.api.author.Author
import model.api.catalog.Catalog
import mui.material.*
import mui.system.ResponsiveStyleValue
import mui.system.sx
import npm.react.window.ListChildComponentProps
import npm.react.window.VariableSizeList
import npm.react.window.VariableSizeListClass
import npm.react.window.VariableSizeListProps
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.ButtonType
import react.dom.html.HTMLAttributes
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li

//private data class Item(val id: Int, val name: String)
//private val ITEMS = (1..10000).map { Item(it, "name: ${(it + 1) * 2}") }
//private val ITEMS2 = (10000..20000).map { Item(it, "namemew: ${(it + 1) * 2}") }
//private val ItemRow = FC<ListChildComponentProps<Item>> { props ->
//    li {
//        +props.data.name
//    }
//}
//private val TestFc = FC<Props> {
//    var stateful by useState { ITEMS }
//    LaunchedEffect(Unit) {
//        var i = 0
//        while (true) {
//            delay(5000L)
//            stateful = if (++i % 2 == 0) ITEMS else ITEMS2
//        }
//    }
//
////    @Suppress("UPPER_BOUND_VIOLATED")
////    VariableSizeList<VariableSizeListProps<Item>> {
////        height = number(300.0)
////        width = number(200.0)
////        itemCount = stateful.size
////        itemSize = { i-> number(if (stateful[i].id % 3 == 0) 100.0 else 60.0) }
////        itemKey = { i -> console.log("fetch key for :$i"); stateful[i].id.toString() }
////
////        children = rawForwardRef<ListChildComponentProps<Item>, PropsWithRef<ListChildComponentProps<Item>>> { props, ref ->
////            console.log(props)
////            FC<ListChildComponentProps<Item>> { iProps ->
////                div {
////                    +iProps
////                    ItemRow {
////                        +iProps
////                        data = stateful[iProps.index]
////                    }
////                }
////            }.create {
////                this.ref = ref
////                +props
////            }
////        }.unsafeCast<ReactNode>()
////    }
//}
//
//private fun useResetCache(data: Any): RefObject<VariableSizeListClass<*>> {
//    val ref = useRef<VariableSizeListClass<*>>(null)
//    useEffect(data) {
//        use(ref.current) { it.resetAfterIndex(0, true) }
//    }
//    return ref
//}
//private val OuterElementContext = createContext(jso<Props>())
//private val OuterElementType = rawForwardRef<HTMLDivElement, PropsWithRef<HTMLDivElement>> { props, ref ->
//    val outerProps = useContext(OuterElementContext)
//    div.create {
//        this.ref = ref
//        +props
//        +outerProps
//    }
//}
//
//
//@Suppress("UPPER_BOUND_VIOLATED")
//private val ListBoxComponent = rawForwardRef<HTMLDivElement, HTMLAttributes<HTMLElement>> { props, ref ->
//    val children = props.children.unsafeCast<Array<ReactNode>>()
//    val itemData = children.flatMap { item ->
//        listOf(item) + (item.asDynamic().children as? Array<ReactNode>).orEmpty()
//    }
//
//    val itemCount = itemData.size
//    val itemSize = 48
//    val getChildSize = { child: ReactNode -> itemSize }
//    val getHeight = {
//        if (itemCount > 8) 8 * itemSize
//        else itemData.map(getChildSize).sum()
//    }
//
//    val gridRef = useResetCache(itemCount)
//
//    div.create {
//        this.ref = ref
//        OuterElementContext.Provider {
//            value = props
//
//            @Suppress("UPPER_BOUND_VIOLATED")
//            VariableSizeList {
//                this.itemData = itemData
//                height = number((getHeight() + 12.0))
//                width = 100.pct
//                this.ref = gridRef
//                outerElementType = OuterElementType
//                innerElementType = "ul"
//                this.itemSize = { i -> number(getChildSize(itemData[i]).toDouble()) }
//                overscanCount = 5
//                this.itemCount = itemCount
//                this.children = "li".unsafeCast<ReactNode>()
//            }
//        }
//    }
//}
//private val TestEnd = FC<Props> {
//    var stateful by useState(ITEMS.toTypedArray())
////    LaunchedEffect(Unit) {
////        var i = 0
////        while (true) {
////            delay(5000L)
////            stateful = if (++i % 2 == 0) ITEMS.toTypedArray() else ITEMS2.toTypedArray()
////        }
////    }
//    @Suppress("UPPER_BOUND_VIOLATED")
//    Autocomplete<AutocompleteProps<Item>> {
//        id = "virtualize"
//        sx { width = 300.px }
//        disableListWrap = true
//        options = stateful
//        renderInput = { params ->
//            TextField.create {
//                +params
//                label = ReactNode("Virtualized")
//                variant = FormControlVariant.outlined
//                fullWidth = true
//            }
//        }
//        ListboxComponent = ListBoxComponent
//        getOptionLabel = { it.name }
//        isOptionEqualToValue = { t1, t2 -> t1.id == t2.id }
//    }
//}

external interface AuthorAutocompleteProps : Props {
    var value: Set<Author>
    var onChange: (Set<Author>) -> Unit
}
val AuthorAutocomplete = FC<AuthorAutocompleteProps> { props ->
    val service by inject<ApiService>()

    @Suppress("UPPER_BOUND_VIOLATED")
    ApiAutocomplete<ApiAutocompleteProps<Author>> {
        label = "Authors"
        value = props.value
        onChange = props.onChange

        fetch = { service.author.getList() }
        comparator = { t1, t2 -> t1.id == t2.id }
        represent = { t -> t.name }
        dialog = FormDialog
    }
}

private val FormDialog = FC<ApiAutocompleteDialogProps<Author>> { props ->
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
