# helsearbeidsgiver-tokenprovider

### ⚠️ **Dette biblioteket er utdatert. Bruken har blitt erstattet av Nais-verktøyet [Texas](https://doc.nav.cloud.nais.io/auth/explanations/#texas).** ⚠️

Denne pakken inneholder hjelpemetoder for å hente OAuth2-token for tilgang mellom apper (med grant type "client_credentials").

Verdiene som er påkrevd i `OAuth2Environment` finnes typisk som systemvaribler.
Systemvariablene stammer fra en Kubernetes-secret som automatisk legges til Nais-apper som har aktivert Azure AD
([se hvordan her](https://doc.nav.cloud.nais.io/reference/application-spec/?h=azure#azureapplicationenabled)).
Kubernetes-secreten vil hete `azure-<appnavn>-<id>` og inneholde systemvariablene som leses i eksempelet nedenfor.

```kt
import no.nav.helsearbeidsgiver.tokenprovider.OAuth2Environment
import no.nav.helsearbeidsgiver.tokenprovider.oauth2ClientCredentialsTokenGetter

val oauth2Environment = OAuth2Environment(
    scope = "api://dev-gcp.eksempel-scope/.default",
    wellKnownUrl = "AZURE_APP_WELL_KNOWN_URL".let(System::getenv),
    tokenEndpointUrl = "AZURE_OPENID_CONFIG_TOKEN_ENDPOINT".let(System::getenv),
    clientId = "AZURE_APP_CLIENT_ID".let(System::getenv),
    clientSecret = "AZURE_APP_CLIENT_SECRET".let(System::getenv),
    clientJwk = "AZURE_APP_JWK".let(System::getenv)
)

val tokenGetter = oauth2ClientCredentialsTokenGetter(oauth2Environment)

val accessToken = tokenGetter()
```
