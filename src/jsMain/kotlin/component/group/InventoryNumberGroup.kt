package component.group

import component.input.BaseTextField
import csstype.*
import domain.api.ApiService
import domain.base.LaunchedEffect
import domain.koin.inject
import domain.onClickEvent
import domain.use
import kotlinx.js.jso
import model.api.catalog.Catalog
import model.api.publication.PublicationInventoryNumber
import model.common.IntId
import mui.material.*
import mui.material.Size
import mui.system.ResponsiveStyleValue
import mui.system.sx
import react.*
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.em

external interface InventoryNumberGroupProps : Props {
    var publicationId: Int?
    var value: Set<PublicationInventoryNumber>
    var onChange: (Set<PublicationInventoryNumber>) -> Unit
}

val InventoryNumberGroup = FC<InventoryNumberGroupProps> { props ->
    val service by inject<ApiService>()
    var catalogs by useState<List<Catalog>>(emptyList())
    LaunchedEffect {
        kotlin.runCatching {
            catalogs = service.catalog.getList()
        }
    }

    var catalog by useState<Catalog?>(null)
    var input by useState("")

    Paper {
        sx { width = 100.pct }

        Stack {
            Stack {
                sx {
                    width = 100.pct
                    alignItems = AlignItems.center
                }
                spacing = ResponsiveStyleValue(1)
                direction = ResponsiveStyleValue(StackDirection.row)


                FormControl {
                    val labelId = "form-control-select-catalog-label"
                    sx { minWidth = 120.px }

                    InputLabel {
                        id = labelId
                        +"Catalog"
                    }
                    @Suppress("UPPER_BOUND_VIOLATED")
                    Select<SelectProps<String>> {
                        this.labelId = labelId
                        id = labelId

                        label = ReactNode("Catalog *")
                        value = catalog?.id?.toString().orEmpty()
                        onChange = { event: dynamic ->
                            val id = event.target.value.unsafeCast<String?>()
                                ?.toIntOrNull()
                            catalog = catalogs.firstOrNull { it.id == id }
                        }

                        val none = arrayOf(
                            MenuItem.create {
                                key = "-1"
                                value = ""
                                selected = false
                                em { +"None" }
                            }
                        )
                        children = ReactNode(
                            none + catalogs.map { item ->
                                MenuItem.create {
                                    key = item.id.toString()
                                    value = item.id.toString()
                                    selected = item.id == catalog?.id
                                    +item.name
                                }
                            }.toTypedArray()
                        )
                    }
                }

                BaseTextField {
                    label = "Text *"
                    limit = 15
                    value = input
                    onChange = { input = it }
                    override = jso {
                        sx { flexGrow = number(1.0) }
                    }
                }

                Button {
                    +"Add"
                    size = Size.medium
                    variant = ButtonVariant.outlined
                    disabled = catalog == null || input.isBlank()
                    onClickEvent {
                        use(input, catalog) { i, c ->
                            val new = PublicationInventoryNumber(
                                publicationId = props.publicationId ?: IntId.Default,
                                catalogId = c.id,
                                text = i,
                            )
                            props.onChange(props.value + new)
                        }
                    }
                }
            }
            Box {
                sx {
                    display = Display.flex
                    justifyContent = JustifyContent.flexStart
                    flexWrap = FlexWrap.wrap
                    padding = Padding(1.em, 1.em)
                    margin = 0.0.em
                }
                component = ReactHTML.ul

                props.value.forEach { item ->
                    val found = catalogs.firstOrNull { it.id == item.catalogId }
                    Chip {
                        label = ReactNode("${found?.letter ?: ""}-${item.text}")
                        onDelete = { props.onChange(props.value - item) }
                        sx {
                            margin = 4.px
                        }
                    }
                }
            }
        }
    }
}