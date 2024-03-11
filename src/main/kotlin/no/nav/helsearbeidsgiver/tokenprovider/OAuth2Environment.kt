package no.nav.helsearbeidsgiver.tokenprovider

import com.nimbusds.oauth2.sdk.GrantType
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod
import no.nav.security.token.support.client.core.ClientAuthenticationProperties
import no.nav.security.token.support.client.core.ClientProperties
import java.net.URI

data class OAuth2Environment(
    val scope: String,
    val wellKnownUrl: String,
    val tokenEndpointUrl: String,
    val clientId: String,
    val clientSecret: String,
    val clientJwk: String,
) {
    internal fun toClientCredentialsProperties(): ClientProperties =
        ClientProperties(
            tokenEndpointUrl = tokenEndpointUrl.let(::URI),
            wellKnownUrl = wellKnownUrl.let(::URI),
            grantType = GrantType.CLIENT_CREDENTIALS,
            scope = scope.split(","),
            authentication =
                ClientAuthenticationProperties(
                    clientId = clientId,
                    clientAuthMethod = ClientAuthenticationMethod.CLIENT_SECRET_POST,
                    clientSecret = clientSecret,
                    clientJwk = clientJwk,
                ),
            resourceUrl = null,
            tokenExchange = null,
        )
}
