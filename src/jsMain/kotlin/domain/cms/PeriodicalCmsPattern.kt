package domain.cms

import io.ktor.client.*
import io.ktor.client.request.*
import model.api.periodical.Periodical

class PeriodicalCmsPattern(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : CmsPattern<Periodical> by httpClient.cmsPattern(baseUrl) {

    suspend fun getSimpleList(): List<Periodical> = httpClient.get("$baseUrl/list/simple")
}