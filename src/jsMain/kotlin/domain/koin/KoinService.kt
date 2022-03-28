package domain.koin

import Cfg
import domain.api.ApiService
import domain.auth.AuthService
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import route.admin.authors.AdminAuthorsViewModel
import route.admin.publications.AdminPublicationsViewModel
import route.admin.users.AdminUsersViewModel
import route.base.login.LoginViewModel

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = GlobalContext.get().inject(qualifier, mode, parameters)

inline fun <reified T : Any> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = GlobalContext.get().get<T>(qualifier, parameters)


actual fun KoinApplication.setupKoinPlatform() {
    modules(
        module {
            single { provideHttpClient(get(), get()) }
            single { provideApiService(get()) }
            single { provideAuthService() }
        },
        module {
            viewModel { LoginViewModel() }

            viewModel { AdminAuthorsViewModel(get()) }
            viewModel { AdminUsersViewModel(get()) }

            viewModel { AdminPublicationsViewModel(get()) }
        }
    )
}

internal fun provideHttpClient(json: Json, authService: AuthService) =
    HttpClient(Js) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        defaultRequest {
            port = Cfg.ApiPort
            headers {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer ${authService.token}")
            }
        }
    }

internal fun provideApiService(httpClient: HttpClient) =
    ApiService(httpClient)

internal fun provideAuthService() =
    AuthService()