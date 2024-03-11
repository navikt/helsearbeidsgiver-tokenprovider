package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.HttpClient
import no.nav.security.token.support.client.core.oauth2.ClientCredentialsGrantRequest
import no.nav.security.token.support.client.core.oauth2.ClientCredentialsTokenClient

/** @param httpClient Dersom egendefinert klient brukes så kan gal konfigurasjon føre til feil. */
fun oauth2ClientCredentialsTokenGetter(
    env: OAuth2Environment,
    httpClient: HttpClient = createHttpClient(),
): () -> String {
    val tokenClient = TokenClient(httpClient).let(::ClientCredentialsTokenClient)

    val request = env.toClientCredentialsProperties().let(::ClientCredentialsGrantRequest)

    return {
        tokenClient.getTokenResponse(request).accessToken
            ?: throw MissingAccessTokenException()
    }
}

class MissingAccessTokenException : Exception()
