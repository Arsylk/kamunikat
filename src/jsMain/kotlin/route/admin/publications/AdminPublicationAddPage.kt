package route.admin.publications

import component.autocomplete.AuthorAutocomplete
import component.autocomplete.UserTagAutocomplete
import csstype.FlexGrow
import csstype.Padding
import csstype.em
import domain.onValueChanged
import domain.update
import domain.use
import kotlinx.datetime.Clock
import kotlinx.js.jso
import model.api.Publication
import model.api.author.Author
import model.api.user.AddUserRequest
import mui.material.*
import mui.system.ResponsiveStyleValue
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.router.RouteProps
import route.Route
import route.admin.AdminPageRoute

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

    Box {
        sx = jso {
            flexGrow = FlexGrow(1.0)
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

                    // TODO [pub.position]


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
            inputProps = jso<dynamic> { maxLength = limit } as? InputBaseComponentProps
        }
        value = props.value
        onValueChanged(props.onChange)
    }
}