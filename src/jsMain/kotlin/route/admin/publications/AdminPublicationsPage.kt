package route.admin.publications

import component.datatable.Datatable
import component.datatable.DatatableProps
import component.datatable.bindSource
import component.datatable.cell.SimpleDatatableCell
import component.datatable.header.DatatableHeaderCell
import component.datatable.header.DatatableHeaderProps
import component.datatable.row.DatatableRowProps
import domain.base.useViewModel
import model.api.publication.Publication
import model.api.publication.PublicationField
import model.common.Order
import model.common.cycle
import react.FC
import react.Props
import react.create
import react.router.RouteProps
import react.router.useNavigate
import route.Route
import route.admin.AdminPageRoute

object AdminPublicationsPageRoute : Route {
    override val path = "publications"
    override val name = "Publications"
    override val parent = AdminPageRoute

    override fun create(props: RouteProps) = AdminPublicationsPage.create()
}

val AdminPublicationsPage = FC<Props> {
    val navigate = useNavigate()
    val viewModel by useViewModel<AdminPublicationsViewModel>()


    @Suppress("UPPER_BOUND_VIOLATED")
    Datatable<DatatableProps<PublicationField, Publication>> {
        bindSource(viewModel.source)

        title = "Publications"
        keySelector = { it.id }
        onAdd = { navigate(AdminPublicationAddPageRoute.absolutePath) }

        RenderHeader = RenderPublicationHeader
        RenderRow = RenderPublicationRow
    }
}

@Suppress("UPPER_BOUND_VIOLATED")
private val RenderPublicationHeader = FC<DatatableHeaderProps<PublicationField>> { props ->
    for (field in arrayOf(PublicationField.Id, PublicationField.Title)) {
        DatatableHeaderCell {
            val isActive = props.orderSelect == field
            text = field.name
            sortable = true
            active = isActive
            order = props.order
            onClickEvent = {
                val newOrder = if (isActive) props.order.cycle() else Order.Ascending
                props.onOrderChange?.invoke(newOrder, field)
            }
        }
    }
    DatatableHeaderCell {
        text = "Tags"
        sortable = false
    }
}

@Suppress("UPPER_BOUND_VIOLATED")
private val RenderPublicationRow = FC<DatatableRowProps<Publication>> { props ->
    SimpleDatatableCell {
        text = props.item.id.toString()
    }
    SimpleDatatableCell {
        text = props.item.title
    }
}