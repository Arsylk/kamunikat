package domain.auth

import domain.api.ApiService
import domain.common.getValue
import domain.common.localStorage
import domain.common.setValue
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import model.user.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthService : KoinComponent {
    private val apiService by inject<ApiService>()
    private val channel = Channel<Unit>(Channel.CONFLATED)
    private val channelFlow = channel.consumeAsFlow()

    var token by localStorage(TOKEN_KEY)
    var user by localStorage<User>(USER_KEY)
        private set

    suspend fun login(email: String, password: String) {
        val resp = apiService.login(email, password)
        token = resp.token
        user = resp.user
        channel.send(Unit)
    }

    suspend fun logout() {
        apiService.logout()
        user = null
        token = null
        channel.send(Unit)
    }

    fun userFlow() = channelFlow
        .onStart { emit(Unit) }
        .map { user }


    companion object {
        private const val TOKEN_KEY = "token"
        private const val USER_KEY = "user"
    }
}