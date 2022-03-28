package domain.datatable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import model.api.PaginatedRequest
import model.api.PaginatedResponse
import model.common.Order
import model.common.Sortable

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DatatableSource<Item: Sortable<Field>, Field: Enum<Field>>(
    private val initialPage: Int,
    private val initialPerPage: Int,
) {
    private val request = MutableStateFlow<DatasourceRequest<Field>>(
        DatasourceRequest(page = initialPage, perPage = initialPerPage, order = null, orderSelect = null)
    )
    private val _operation = MutableStateFlow<Operation>(Operation.Idle)
    val operation = _operation.asStateFlow()
    val state = channelFlow {
        request.collectLatest { req ->
            _operation.value = Operation.Loading

            val result = runCatching { fetch(req.page, req.perPage, req.order, req.orderSelect) }
            currentCoroutineContext().ensureActive()
            result.onSuccess { resp ->
                val state = State(
                    page = resp.page,
                    perPage = resp.perPage,
                    order = resp.order,
                    orderSelect = resp.orderSelect,
                    fullSize = resp.fullSize,
                    items = resp.items,
                )
                send(state)
                _operation.value = Operation.Idle
            }
            result.onFailure { t ->
                _operation.value = Operation.Error(t)
            }
        }
    }
    val initialState = State<Item, Field>(
        page = initialPage,
        perPage = initialPerPage,
        order = null,
        orderSelect = null,
        fullSize = null,
        items = emptyList(),
    )

    fun stateIn(scope: CoroutineScope): StateFlow<State<Item, Field>> = state.stateIn(
        scope, SharingStarted.WhileSubscribed(), initialState
    )

    fun setPage(page: Int) { request.update { it.copy(page = page) } }

    fun setPerPage(perPage: Int) { request.update { it.copy(perPage = perPage) } }

    fun setOrder(order: Order?) { request.update { it.copy(order = order) } }

    fun setOrderSelect(orderSelect: Field?) { request.update { it.copy(orderSelect = orderSelect) } }

    fun setFullOrder(order: Order?, orderSelect: Field?) { request.update { it.copy(order = order, orderSelect = orderSelect) } }


    /* @Throws(Throwable::class) */
    protected abstract suspend fun fetch(
        page: Int,
        perPage: Int,
        order: Order?,
        orderSelect: Field?,
    ): PaginatedResponse<Item, Field>


    private data class DatasourceRequest<T: Enum<T>>(
        val page: Int,
        val perPage: Int,
        val order: Order?,
        val orderSelect: T?,
    )

    data class State<Item: Sortable<Field>, Field: Enum<Field>>(
        val page: Int,
        val perPage: Int,
        val order: Order?,
        val orderSelect: Field?,
        val fullSize: Int?,
        val items: List<Item>,
    )

    sealed class Operation {
        object Idle : Operation()
        object Loading : Operation()
        data class Error(val throwable: Throwable) : Operation()
    }
}

fun <Item: Sortable<Field>, Field : Enum<Field>> DatatableSource(
    page: Int = PaginatedRequest.DefaultPage,
    perPage: Int = PaginatedRequest.DefaultPerPage,
    fetch: suspend (Int, Int, Order?, Field?) -> PaginatedResponse<Item, Field>,
) = object : DatatableSource<Item, Field>(page, perPage) {
    override suspend fun fetch(
        page: Int,
        perPage: Int,
        order: Order?,
        orderSelect: Field?,
    ): PaginatedResponse<Item, Field> = fetch(page, perPage, order, orderSelect)
}
