package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import no.nav.helsearbeidsgiver.utils.test.json.removeJsonWhitespace
import no.nav.helsearbeidsgiver.utils.test.resource.readResource

object Mock {
    val oauth2Environment =
        OAuth2Environment(
            scope = "scope1,scope2",
            wellKnownUrl = "http://well.known.url",
            tokenEndpointUrl = "http://token.endpoint.url",
            clientId = "clientId",
            clientSecret = "clientSecret",
            clientJwk = "mockJwkPublicKey.json".readResource(),
        )

    fun oauth2AccessTokenResponseJson(accessToken: String): String =
        """
        {
            "access_token": "$accessToken"
        }
        """
            .removeJsonWhitespace()

    fun httpClient(
        status: HttpStatusCode,
        content: String,
    ): HttpClient {
        val mockEngine =
            MockEngine {
                respond(
                    content = content,
                    status = status,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                )
            }

        return HttpClient(mockEngine) {
            customConfig()
        }
    }
}
