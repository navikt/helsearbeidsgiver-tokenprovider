package no.nav.helsearbeidsgiver.tokenprovider

interface AccessTokenProvider {
    fun getToken(): String
}

