package no.nav.helsearbeidsgiver.tokenprovider

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson

internal fun createHttpClient(): HttpClient = HttpClient(OkHttp) { configureClientConfig() }

internal fun HttpClientConfig<*>.configureClientConfig() {
    expectSuccess = true
    install(ContentNegotiation) {
        jackson {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            registerModule(JavaTimeModule())
        }
    }
}

internal fun String.readResource(): String = ClassLoader.getSystemResource(this).readText()
