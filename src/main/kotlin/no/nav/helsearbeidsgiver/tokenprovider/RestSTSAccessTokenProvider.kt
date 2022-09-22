package no.nav.helsearbeidsgiver.tokenprovider

import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.time.Instant
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
    private val username: String,
    private val password: String,
    stsEndpoint: String
) : AccessTokenProvider {

    private val httpClient = createHttpClient()
    private val endpointURI: String

    private var currentToken: JwtToken

    init {
        endpointURI = "$stsEndpoint?grant_type=client_credentials&scope=openid"
        currentToken = runBlocking { requestToken() }
    }

    override fun getToken(): String {
        if (isExpired(currentToken, Date.from(Instant.now().plusSeconds(300)))) {
            logger.debug("OIDC Token is expired, getting a new one from the STS")
            currentToken = runBlocking { requestToken() }
            logger.debug("Hentet nytt token fra sts som går ut ${currentToken.expirationTime}")
        }
        return currentToken.tokenAsString
    }

    private suspend fun requestToken(): JwtToken {
        val response = runBlocking {
            httpClient.get(endpointURI) {
                header(HttpHeaders.Accept, ContentType.Application.Json.toString())
                basicAuth(username = username, password = password)
            }
        }
            .body<STSOidcResponse>()

        return JwtToken(response.access_token)
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
        private val logger = LoggerFactory.getLogger(RestSTSAccessTokenProvider::class.java)
    }
}
