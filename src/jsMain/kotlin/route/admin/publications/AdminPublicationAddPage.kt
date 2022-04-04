package route.admin.publications

import component.autocomplete.AuthorAutocomplete
import component.autocomplete.CatalogAutocomplete
import component.autocomplete.CategoryAutocomplete
import csstype.Padding
import csstype.em
import csstype.number
import domain.onValueChanged
import domain.update
import domain.use
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinInstant
import kotlinx.js.jso
import model.api.publication.Publication
import model.api.author.Author
import model.api.catalog.Catalog
import model.api.publication.AddPublicationRequest
import model.common.ids
import mui.lab.DatePicker
import mui.lab.LocalizationProvider
import mui.material.*
import mui.system.ResponsiveStyleValue
import npm.date.fns.localePl
import npm.mui.lab.AdapterDateFns
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.router.RouteProps
import route.Route
import route.admin.AdminPageRoute
import kotlin.js.Date
import model.api.category.Category

object AdminPublicationAddPageRoute : Route {
    override val path = "publication/add"
    override val name = "Add Publication"
    override val parent = AdminPageRoute

    override fun create(props: RouteProps) = AdminPublicationAddPage.create()
}

val AdminPublicationAddPage = FC<Props> {
    val (pub, update) = useState {
        Publication(
            id = 0,
            isPublished = false,
            isPeriodical = false,
            isPlanned = false,
            isLicenced = false,
            position = 0,
            title = "",
            subtitle = "",
            originalTitle = "",
            coAuthor = "",
            translator = "",
            edition = "",
            volume = "",
            redactor = "",
            college = "",
            illustrator = "",
            place = "",
            series = "",
            year = null,
            publishingHouse = "",
            publisher = "",
            dimensions = "",
            signature = "",
            isbn = "",
            issn = "",
            ukd = "",
            copyright = "",
            shopUrl = "",
            origin = "",
            extra = "",
            description = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
        )
    }
    val (addPub, addUpdate) = useState {
        AddPublicationRequest(publication = pub)
    }
    useEffect(pub) { addUpdate.update { copy(publication = pub) } }


    Box {
        sx = jso {
            flexGrow = number(1.0)
            padding = Padding(2.em, 2.em)
        }
        Paper {
            sx = jso {
                padding = Padding(1.em, 1.em)
            }
            Typography {
                component = ReactHTML.h1
                variant = "h5"
                +"Add publication"
            }

            Box {
                component = ReactHTML.form
                onSubmit = { event ->
                    event.preventDefault()
                    console.log(pub)
                }

                Grid {
                    container = true
                    spacing = ResponsiveStyleValue(2)
                    rowSpacing = ResponsiveStyleValue(2)

                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Published"
                            isChecked = pub.isPublished
                            onChange = { update.update { copy(isPublished = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Periodical"
                            isChecked = pub.isPeriodical
                            onChange = { update.update { copy(isPeriodical = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Planned"
                            isChecked = pub.isPlanned
                            onChange = { update.update { copy(isPlanned = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Licenced"
                            isChecked = pub.isLicenced
                            onChange = { update.update { copy(isLicenced = it) } }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseNumberField {
                            label = "Position"
                            value = pub.position
                            onChange = { update.update { copy(position = it) } }
                        }
                    }


                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Title"
                            limit = 255
                            required = true

                            value = pub.title
                            onChange = { update.update { copy(title = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Subtitle"
                            limit = 255

                            value = pub.subtitle
                            onChange = { update.update { copy(subtitle = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Original Title"
                            limit = 255

                            value = pub.originalTitle
                            onChange = { update.update { copy(originalTitle = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        var authors by useState(emptySet<Author>())
                        useEffect(authors) {
                            addUpdate.update { copy(authorIds = authors.ids) }
                        }
                        AuthorAutocomplete {
                            value = authors
                            onChange = { authors = it }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Co Author"
                            limit = 255

                            value = pub.coAuthor
                            onChange = { update.update { copy(coAuthor = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Translator"
                            limit = 255

                            value = pub.translator
                            onChange = { update.update { copy(translator = it) } }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Edition"
                            limit = 63

                            value = pub.edition
                            onChange = { update.update { copy(edition = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Volume"
                            limit = 31

                            value = pub.volume
                            onChange = { update.update { copy(volume = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Redactor"
                            limit = 63

                            value = pub.redactor
                            onChange = { update.update { copy(redactor = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Illustrator"
                            limit = 255

                            value = pub.illustrator
                            onChange = { update.update { copy(illustrator = it) } }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Place"
                            limit = 127

                            value = pub.place
                            onChange = { update.update { copy(place = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Series"
                            limit = 255

                            value = pub.series
                            onChange = { update.update { copy(series = it) } }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseDateField {
                            label = "Year"
                            value = pub.year
                            onChange = { update.update { copy(year = it) } }
                        }
                    }


                    Grid {
                        item = true
                        xs = 6
                        var catalogs by useState(emptySet<Catalog>())
                        useEffect(catalogs) { addUpdate.update { copy(catalogIds = catalogs.ids) } }
                        CatalogAutocomplete {
                            value = catalogs
                            onChange = { catalogs = it }
                        }
                    }
                    Grid {
                        item = true
                        xs = 6
                        var categories by useState(emptySet<Category>())
                        useEffect(categories) { addUpdate.update { copy(categoryIds = categories.ids) } }
                        CategoryAutocomplete {
                            value = categories
                            onChange = { categories = it }
                        }
                    }
                    Grid {
                        container = true
                        direction = ResponsiveStyleValue(GridDirection.rowReverse)
                        Grid {
                            item = true
                            xs = "auto"
                            Button {
                                type = ButtonType.submit
                                variant = ButtonVariant.contained
                                +"Add"
                            }
                        }
                    }
                }
            }
        }
    }
}

private external interface BaseCheckboxProps : Props {
    var label: String
    var isChecked: Boolean
    var onChange: (Boolean) -> Unit
}
private val BaseCheckbox = FC<BaseCheckboxProps> { props ->
    FormControlLabel {
        label = ReactNode(props.label)
        control = Checkbox.create {
            checked = props.isChecked
            onChange = { _, checked ->
                props.onChange(checked)
            }
        }
    }
}

private external interface BaseTextFieldProps : Props {
    var label: String
    var value: String
    var onChange: (String) -> Unit
    var required: Boolean?
    var limit: Int?
}
private val BaseTextField = FC<BaseTextFieldProps> { props ->
    TextField {
        fullWidth = true
        required = props.required == true
        label = ReactNode(props.label)
        type = InputType.text
        use(props.limit) { limit ->
            inputProps = jso<dynamic> { maxLength = limit }.unsafeCast<InputBaseComponentProps>()
        }
        value = props.value
        onValueChanged(props.onChange)
    }
}

private external interface BaseNumberFieldProps : Props {
    var label: String
    var value: Int
    var onChange: (Int) -> Unit
    var required: Boolean?
}
private val BaseNumberField = FC<BaseNumberFieldProps> { props ->
    TextField {
        fullWidth = true
        required = props.required == true
        label = ReactNode(props.label)
        type = InputType.number
        value = props.value
        onValueChanged { props.onChange(it.toIntOrNull() ?: return@onValueChanged) }
    }
}

private external interface BaseDateFieldProps : Props {
    var label: String
    var value: LocalDate?
    var onChange: (LocalDate?) -> Unit
}
private val BaseDateField = FC<BaseDateFieldProps> { props ->
    LocalizationProvider {
        dateAdapter = AdapterDateFns
        locale = localePl

        var jsDate by useState<Date?>(null)
        useEffect(props.value) {
            jsDate = use(props.value) {
                Date(year = it.year, month = it.monthNumber - 1, day = it.dayOfMonth)
            }
        }


        DatePicker {
            asDynamic().value = jsDate

            asDynamic().mask = "__.__.____"
            asDynamic().label = props.label
            asDynamic().onChange = { any: dynamic ->
                when (any) {
                    is Date -> {
                        if (!any.getTime().isNaN()) {
                            props.onChange(
                                LocalDate(any.getFullYear(), any.getMonth() + 1, any.getDate())
                            )
                        }
                    }
                    else -> props.onChange(null)
                }
            }
            asDynamic().renderInput = { params: Props ->
                TextField.create {
                    +params
                    onBlur = {
                        val currentString = params.asDynamic().inputProps.value
                            .unsafeCast<String?>().orEmpty()
                        currentString.runCatching {
                            LocalDate(
                                year = substring(6..9).toInt(),
                                monthNumber = substring(3..4).toInt(),
                                dayOfMonth = substring(0..1).toInt(),
                            )
                        }.onFailure {
                            jsDate = null
                            props.onChange(null)
                        }
                    }
                }
            }
        }
    }
}