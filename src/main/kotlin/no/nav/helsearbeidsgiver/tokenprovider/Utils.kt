package no.nav.helsearbeidsgiver.tokenprovider

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

internal fun createHttpClient(): HttpClient =
    HttpClient(OkHttp) { configureJsonHandler().also { expectSuccess = true } }

@OptIn(ExperimentalSerializationApi::class)
internal fun HttpClientConfig<*>.configureJsonHandler() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }
}

internal fun String.readResource(): String =
    ClassLoader.getSystemResource(this).readText()
