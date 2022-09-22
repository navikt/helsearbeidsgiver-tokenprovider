package no.nav.helsearbeidsgiver.tokenprovider

import no.nav.security.token.support.client.core.context.JwtBearerTokenResolver
import no.nav.security.token.support.v2.TokenValidationContextPrincipal
import java.util.Optional

class TokenResolver : JwtBearerTokenResolver {
    var tokenPrincipal: TokenValidationContextPrincipal? = null

    override fun token(): Optional<String> {
        return tokenPrincipal?.context?.firstValidToken?.map { it.tokenAsString } ?: Optional.empty()
    }
}
