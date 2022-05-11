package no.nav.helsearbeidsgiver.tokenprovider

fun String.loadFromResources(): String {
    return ClassLoader.getSystemResource(this).readText()
}
