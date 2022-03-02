package route.admin.users

import domain.base.collectAsState
import domain.base.useViewModel
import react.FC
import react.Props
import route.Route
import route.admin.AdminPageRoute

object AdminUsersPageRoute : Route {
    override val path = "users"
    override val name = "Users"
    override val parent = AdminPageRoute
}

val AdminUsersPage = FC<Props> {
    val viewModel by useViewModel<AdminUsersViewModel>()
    val list = viewModel.source.current.collectAsState()
}