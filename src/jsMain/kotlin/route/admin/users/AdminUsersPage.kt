package route.admin.users

import component.datatable.*
import component.datatable.cell.SimpleDatatableCell
import component.datatable.header.DatatableHeaderCell
import component.datatable.header.DatatableHeaderProps
import component.datatable.row.DatatableRowProps
import domain.base.collectAsState
import domain.base.useViewModel
import domain.datatable.DatatableSource
import model.common.Order
import model.common.cycle
import model.user.User
import model.user.UserField
import mui.material.Chip
import mui.material.ChipVariant
import mui.material.TableCell
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.html.ReactHTML
import react.router.RouteProps
import react.router.useNavigate
import route.Route
import route.admin.AdminPageRoute

object AdminUsersPageRoute : Route {
    override val path = "users"
    override val name = "Users"
    override val parent = AdminPageRoute

    override fun create(props: RouteProps) = AdminUsersPage.create()
}

val AdminUsersPage = FC<Props> {
    val navigate = useNavigate()
    val viewModel by useViewModel<AdminUsersViewModel>()

    @Suppress("UPPER_BOUND_VIOLATED")
    Datatable<DatatableProps<UserField, User>> {
        bindSource(viewModel.source)

        title = "Users"
        keySelector = { it.id }
        onAdd = { navigate(AdminUserAddPageRoute.absolutePath) }

        RenderHeader = RenderUserHeader
        RenderRow = RenderUserRow
    }
}

@Suppress("UPPER_BOUND_VIOLATED")
private val RenderUserHeader = FC<DatatableHeaderProps<UserField>> { props ->
    for (field in arrayOf(UserField.Id, UserField.Username, UserField.Email)) {
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
private val RenderUserRow = FC<DatatableRowProps<User>> { props ->
    SimpleDatatableCell {
        text = props.item.id.toString()
    }
    SimpleDatatableCell {
        text = props.item.username
    }
    SimpleDatatableCell {
        text = props.item.email
    }
    TableCell {
        component = ReactHTML.th
        scope = "row"
        props.item.tags.forEach {
            Chip {
                variant = ChipVariant.outlined
                label = ReactNode(it.name)
            }
        }
    }
}