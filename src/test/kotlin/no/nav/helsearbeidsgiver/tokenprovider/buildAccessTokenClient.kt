package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.http.*

fun buildClient(status: HttpStatusCode, content: String): RestSTSAccessTokenProvider {
    return RestSTSAccessTokenProvider(
        "",
        "",
        "",
        mockHttpClient(status, content)
    )
}

val validStsResponse = "sts-mock-data/valid-sts-token.json".loadFromResources()
