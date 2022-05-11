package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.http.*
import no.nav.helsearbeidsgiver.tokenprovider.RestSTSAccessTokenProvider
import no.nav.helsearbeidsgiver.tokenprovider.mockHttpClient
import no.nav.helse.arbeidsgiver.utils.loadFromResources

fun buildClient(status: HttpStatusCode, content: String): RestSTSAccessTokenProvider {
    return RestSTSAccessTokenProvider(
        "",
        "",
        "",
        mockHttpClient(status, content)
    )
}

val validStsResponse = "sts-mock-data/valid-sts-token.json".loadFromResources()
