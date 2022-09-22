package no.nav.helsearbeidsgiver.tokenprovider

import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.Base64
import java.util.Date

/**
 * STS-klient for å hente access token for bruk i andre tjenester, feks joark, PDL eller Oppgave.
 *
 * Det returnerte tokenet representerer den angitte servicebrukeren (username, password)
 *
 * Cacher tokenet til det 5 minutter unna å bli ugyldig.
 *
 * STS skal fases ut til fordel for OAuth2 Client Credentials og Token Exchange (TokenX)
 */
class RestSTSAccessTokenProvider(
    username: String,
    password: String,
    stsEndpoint: String
) : AccessTokenProvider {

    private val httpClient = createHttpClient()
    private val endpointURI: String
    private val basicAuth: String

    private var currentToken: JwtToken

    init {
        basicAuth = basicAuth(username, password)
        endpointURI = "$stsEndpoint?grant_type=client_credentials&scope=openid"
        currentToken = runBlocking { requestToken() }
    }

    override fun getToken(): String {
        if (isExpired(currentToken, Date.from(Instant.now().plusSeconds(300)))) {
            log.debug("OIDC Token is expired, getting a new one from the STS")
            currentToken = runBlocking { requestToken() }
            log.debug("Hentet nytt token fra sts som går ut ${currentToken.expirationTime}")
        }
        return currentToken.tokenAsString
    }

    private suspend fun requestToken(): JwtToken {
        val response = runBlocking {
            httpClient.get(endpointURI) {
                headers {
                    append("Authorization", basicAuth)
                    append("Accept", "application/json")
                }
            }
        }
            .body<STSOidcResponse>()

        return JwtToken(response.access_token)
    }

    private fun basicAuth(username: String, password: String): String {
        log.debug("basic auth username: $username")
        return "Basic " + Base64.getEncoder().encodeToString("$username:$password".toByteArray())
    }

    private fun isExpired(jwtToken: JwtToken, date: Date): Boolean {
        return date.after(jwtToken.expirationTime) &&
            jwtToken.expirationTime.before(date)
    }

    private class JwtToken(encodedToken: String) {
        val tokenAsString: String = encodedToken
        val jwt: JWT = JWTParser.parse(encodedToken)
        val expirationTime = jwt.jwtClaimsSet.expirationTime
    }

    @Serializable
    private data class STSOidcResponse(
        val access_token: String
    )

    companion object {
        private val log = LoggerFactory.getLogger(RestSTSAccessTokenProvider::class.java)
    }
}
