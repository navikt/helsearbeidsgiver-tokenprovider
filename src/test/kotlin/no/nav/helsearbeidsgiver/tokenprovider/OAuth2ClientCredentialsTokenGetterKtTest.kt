package no.nav.helsearbeidsgiver.tokenprovider

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.mockk
import no.nav.helsearbeidsgiver.utils.test.mock.mockConstructor
import no.nav.security.token.support.client.core.OAuth2ClientException
import no.nav.security.token.support.client.core.oauth2.ClientCredentialsTokenClient
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse

class OAuth2ClientCredentialsTokenGetterKtTest : FunSpec({

    test("tokenGetter henter access token") {
        val accessToken = "You shall pass!"

        val mockHttpClient =
            Mock.httpClient(
                status = HttpStatusCode.OK,
                content = Mock.oauth2AccessTokenResponseJson(accessToken),
            )

        val tokenGetter = oauth2ClientCredentialsTokenGetter(Mock.oauth2Environment, mockHttpClient)

        tokenGetter() shouldBe accessToken
    }

    test("tokenGetter kaster exception dersom access token er tom") {
        mockConstructor(ClientCredentialsTokenClient::class) {
            every { anyConstructed<ClientCredentialsTokenClient>().getTokenResponse(any()) } returns OAuth2AccessTokenResponse()

            val tokenGetter = oauth2ClientCredentialsTokenGetter(Mock.oauth2Environment, mockk())

            shouldThrowExactly<MissingAccessTokenException>(tokenGetter)
        }
    }

    test("tokenGetter kaster exception dersom kall feiler") {
        val mockHttpClient =
            Mock.httpClient(
                status = HttpStatusCode.InternalServerError,
                content = "Trøbbel i tårnet!",
            )

        val tokenGetter = oauth2ClientCredentialsTokenGetter(Mock.oauth2Environment, mockHttpClient)

        shouldThrowExactly<OAuth2ClientException>(tokenGetter)
    }
})
