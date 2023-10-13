package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import no.nav.helsearbeidsgiver.utils.test.mock.mockStatic
import no.nav.helsearbeidsgiver.utils.test.resource.readResource

object MockResponse {
    val validStsResponse = "sts-mock-data/valid-sts-token.json".readResource()
}

fun mockAccessTokenProvider(
    status: HttpStatusCode,
    content: String,
): RestSTSAccessTokenProvider {
    val mockEngine =
        MockEngine {
            respond(
                content = content,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

    val mockHttpClient =
        HttpClient(mockEngine) {
            configureClientConfig()
        }

    return mockStatic(::createHttpClient) {
        every { createHttpClient() } returns mockHttpClient
        RestSTSAccessTokenProvider("", "", "")
    }
}
