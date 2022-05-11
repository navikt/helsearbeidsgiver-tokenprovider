package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.features.ServerResponseException
import io.ktor.http.*
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.test.junit.JUnitAsserter.assertNotNull

class RestSTSAccessTokenProviderTest {

    @Test
    internal fun `valid answer from STS returns valid token, second call gives cached answer`() {
        assertNotNull("", buildClient(HttpStatusCode.OK, validStsResponse).getToken())
        assertNotNull("", buildClient(HttpStatusCode.OK, validStsResponse).getToken())
    }

    @Test
    internal fun `Error response (5xx) from STS throws exception`() {
        assertThrows(ServerResponseException::class.java) {
            buildClient(HttpStatusCode.InternalServerError, "")
        }
    }
}
