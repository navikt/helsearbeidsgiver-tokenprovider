package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.core.http.OAuth2HttpRequest
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse
import org.slf4j.LoggerFactory

@Serializer(forClass = OAuth2AccessTokenResponse::class)
object OAuth2AccessTokenResponseSerializer

class DefaultOAuth2HttpClient() : OAuth2HttpClient {
    private val httpClient = createHttpClient()

    override fun post(oAuth2HttpRequest: OAuth2HttpRequest): OAuth2AccessTokenResponse {
        return runBlocking {
            try {
                val data = httpClient.submitForm(
                    url = oAuth2HttpRequest.tokenEndpointUrl.toString(),
                    formParameters = Parameters.build {
                        oAuth2HttpRequest.formParameters.forEach {
                            append(it.key, it.value)
                        }
                    }
                ).body<String>()
                return@runBlocking Json.decodeFromString(OAuth2AccessTokenResponseSerializer, data)
            } catch (ex: Exception) {
                if (ex is ClientRequestException) {
                    logger.error(ex.response.body<String>())
                }
                throw ex
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DefaultOAuth2HttpClient::class.java)
    }
}
