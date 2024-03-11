package no.nav.helsearbeidsgiver.tokenprovider

import com.nimbusds.oauth2.sdk.GrantType
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class OAuth2EnvironmentTest : FunSpec({

    test("toClientCredentialsProperties") {
        val props = Mock.oauth2Environment.toClientCredentialsProperties()

        props.scope shouldContainExactly listOf("scope1", "scope2")

        props.tokenEndpointUrl.shouldNotBeNull()
        props.tokenEndpointUrl.toString() shouldBe Mock.oauth2Environment.tokenEndpointUrl

        props.grantType shouldBe GrantType.CLIENT_CREDENTIALS

        props.authentication.clientId shouldBe Mock.oauth2Environment.clientId
        props.authentication.clientAuthMethod shouldBe ClientAuthenticationMethod.CLIENT_SECRET_POST
        props.authentication.clientSecret.shouldNotBeNull()
        props.authentication.clientSecret shouldBe Mock.oauth2Environment.clientSecret
        props.authentication.clientJwk.shouldNotBeNull()
        props.authentication.clientJwk shouldBe Mock.oauth2Environment.clientJwk

        props.resourceUrl.shouldBeNull()
        props.tokenExchange.shouldBeNull()
    }
})
