package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlin.reflect.KFunction

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

    return mockFn(::createHttpClient) {
        every { createHttpClient() } returns mockHttpClient
        RestSTSAccessTokenProvider("", "", "")
    }
}

private fun <T> mockFn(
    fn: KFunction<*>,
    block: () -> T,
): T {
    mockkStatic(fn)
    return try {
        block()
    } finally {
        unmockkStatic(fn)
    }
}
