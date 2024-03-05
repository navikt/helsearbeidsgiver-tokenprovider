package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parametersOf
import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.utils.log.logger
import no.nav.helsearbeidsgiver.utils.log.sikkerLogger
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.core.http.OAuth2HttpRequest
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse

internal class TokenClient(
    private val httpClient: HttpClient,
) : OAuth2HttpClient {
    private val logger = logger()
    private val sikkerLogger = sikkerLogger()

    override fun post(req: OAuth2HttpRequest): OAuth2AccessTokenResponse =
        runBlocking {
            try {
                httpClient.submitForm(
                    url = req.tokenEndpointUrl.toString(),
                    formParameters =
                        req.formParameters
                            .mapValues { listOf(it.value) }
                            .let(::parametersOf),
                ).body<OAuth2AccessTokenResponse>()
            } catch (e: Exception) {
                if (e is ClientRequestException) {
                    "Noe gikk galt under henting av av OAuth2-token.".also {
                        logger.error(it)
                        sikkerLogger.error("$it. Error response: ${e.response.body<String>()}")
                    }
                }
                throw e
            }
        }
}
