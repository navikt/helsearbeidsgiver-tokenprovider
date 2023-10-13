package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class RestSTSAccessTokenProviderTest {
    @Test
    internal fun `valid answer from STS returns valid token, second call gives cached answer`() {
        assertNotNull("", mockAccessTokenProvider(HttpStatusCode.OK, MockResponse.validStsResponse).getToken())
        assertNotNull("", mockAccessTokenProvider(HttpStatusCode.OK, MockResponse.validStsResponse).getToken())
    }

    @Test
    internal fun `Error response (5xx) from STS throws exception`() {
        assertThrows(ServerResponseException::class.java) {
            mockAccessTokenProvider(HttpStatusCode.InternalServerError, MockResponse.validStsResponse)
        }
    }
}
