package route.admin.publications

import component.autocomplete.AuthorAutocomplete
import component.autocomplete.CatalogAutocomplete
import component.autocomplete.CategoryAutocomplete
import component.autocomplete.PeriodicalAutocomplete
import component.group.InventoryNumberGroup
import component.input.*
import csstype.Padding
import csstype.em
import csstype.number
import domain.base.useStateCopy
import kotlinx.datetime.Clock
import model.api.author.Author
import model.api.catalog.Catalog
import model.api.category.Category
import model.api.periodical.Periodical
import model.api.publication.AddPublicationRequest
import model.api.publication.Publication
import model.api.publication.PublicationInventoryNumber
import model.common.ids
import mui.material.*
import mui.system.ResponsiveStyleValue
import mui.system.sx
import react.*
import react.dom.html.ButtonType
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
    var pub by useRefValue(
        Publication(
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
    )
    var authors by useState(emptySet<Author>())
    var catalogs by useState(emptySet<Catalog>())
    var categories by useState(emptySet<Category>())
    var periodicals by useState(emptySet<Periodical>())
    var invNum by useState(emptySet<PublicationInventoryNumber>())

    var isPeriodical by useStateCopy(pub.isPeriodical) {
        pub = pub.copy(isPeriodical = it)
        if (!it) periodicals = emptySet()
    }


    Box {
        sx {
            flexGrow = number(1.0)
            padding = Padding(2.em, 2.em)
        }
        Paper {
            sx { padding = Padding(1.em, 1.em) }
            Typography {
                component = ReactHTML.h1
                variant = "h5"
                +"Add publication"
            }

            Box {
                component = ReactHTML.form
                onSubmit = { event ->
                    event.preventDefault()
                    val new = AddPublicationRequest(
                        publication = pub,
                        periodicalIds = periodicals.ids,
                        catalogIds = catalogs.ids,
                        categoryIds = categories.ids,
                        authorIds = authors.ids,
                        inventoryNumbers = invNum,
                    )
                    console.log(new)
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
                            initialValue = false
                            onChange = { pub = pub.copy(isPublished = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Periodical"
                            value = isPeriodical
                            onChange = { isPeriodical = it }
                        }
                    }
                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Planned"
                            initialValue = false
                            onChange = { pub = pub.copy(isPlanned = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 3
                        BaseCheckbox {
                            label = "Licenced"
                            initialValue = false
                            onChange = { pub = pub.copy(isLicenced = it) }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseNumberField {
                            label = "Position"
                            initialValue = pub.position
                            onChange = { pub = pub.copy(position = it) }
                        }
                    }


                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Title"
                            limit = 255
                            required = true

                            initialValue = pub.title
                            onChange = { pub = pub.copy(title = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Subtitle"
                            limit = 255

                            initialValue = pub.subtitle
                            onChange = { pub = pub.copy(subtitle = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Original Title"
                            limit = 255

                            initialValue = pub.originalTitle
                            onChange = { pub = pub.copy(originalTitle = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
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

                            initialValue = pub.coAuthor
                            onChange = { pub = pub.copy(coAuthor = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Translator"
                            limit = 255

                            initialValue = pub.translator
                            onChange = { pub = pub.copy(translator = it) }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Edition"
                            limit = 63

                            initialValue = pub.edition
                            onChange = { pub = pub.copy(edition = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Volume"
                            limit = 31

                            initialValue = pub.volume
                            onChange = { pub = pub.copy(volume = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Redactor"
                            limit = 63

                            initialValue = pub.redactor
                            onChange = { pub = pub.copy(redactor = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "College"
                            limit = 255

                            initialValue = pub.college
                            onChange = {  pub = pub.copy(college = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Illustrator"
                            limit = 255

                            initialValue = pub.illustrator
                            onChange = { pub = pub.copy(illustrator = it) }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Place"
                            limit = 127

                            initialValue = pub.place
                            onChange = { pub = pub.copy(place = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Series"
                            limit = 255

                            initialValue = pub.series
                            onChange = { pub = pub.copy(series = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 6
                        BaseDateField {
                            label = "Year"
                            initialValue = pub.year
                            onChange = { pub = pub.copy(year = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 6
                        CatalogAutocomplete {
                            value = catalogs
                            onChange = { catalogs = it }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        CategoryAutocomplete {
                            value = categories
                            onChange = { categories = it }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        hidden = !isPeriodical
                        PeriodicalAutocomplete {
                            value = periodicals
                            onChange = { periodicals = it }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Publishing House"
                            limit = 255

                            initialValue = pub.publishingHouse
                            onChange = { pub = pub.copy(publishingHouse = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Publisher"
                            limit = 511

                            initialValue = pub.publisher
                            onChange = { pub = pub.copy(publisher = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Dimensions"
                            limit = 31

                            initialValue = pub.dimensions
                            onChange = { pub = pub.copy(dimensions = it) }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Signature"
                            limit = 31

                            initialValue = pub.signature
                            onChange = { pub = pub.copy(signature = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "isbn"
                            limit = 63

                            initialValue = pub.isbn
                            onChange = { pub = pub.copy(isbn = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "issn"
                            limit = 63

                            initialValue = pub.issn
                            onChange = { pub = pub.copy(issn = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "ukd"
                            limit = 255

                            initialValue = pub.ukd
                            onChange = { pub = pub.copy(ukd = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Copyright"
                            limit = 255

                            initialValue = pub.copyright
                            onChange = { pub = pub.copy(copyright = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Shop Url"
                            limit = 255

                            initialValue = pub.shopUrl
                            onChange = { pub = pub.copy(shopUrl = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseTextField {
                            label = "Origin"
                            limit = 255

                            initialValue = pub.origin
                            onChange = { pub = pub.copy(origin = it) }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        BaseMultilineField {
                            label = "Extra"
                            initialValue = pub.extra
                            onChange = { pub = pub.copy(extra = it) }
                        }
                    }
                    Grid {
                        item = true
                        xs = 12
                        BaseMultilineField {
                            label = "Description"
                            initialValue = pub.description
                            onChange = { pub = pub.copy(description = it) }
                        }
                    }

                    Grid {
                        item = true
                        xs = 12
                        InventoryNumberGroup {
                            publicationId = pub.id
                            value = invNum
                            onChange = { invNum = it }
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