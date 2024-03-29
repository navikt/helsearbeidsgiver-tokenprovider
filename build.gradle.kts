import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "no.nav.helsearbeidsgiver"
version = "0.4.0"

plugins {
    kotlin("jvm")
    id("org.jmailen.kotlinter")
    id("maven-publish")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

java {
    withSourcesJar()
}

tasks {
    test {
        useJUnitPlatform()
    }
}

repositories {
    mavenCentral()
    mavenNav("*")
}

dependencies {
    val hagUtilsVersion: String by project
    val jacksonVersion: String by project
    val kotestVersion: String by project
    val ktorVersion: String by project
    val logbackVersion: String by project
    val mockkVersion: String by project
    val nimbusJoseJwtVersion: String by project
    val tokenClientCoreVersion: String by project

    api("io.ktor:ktor-client-core:$ktorVersion")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusJoseJwtVersion")
    implementation("io.ktor:ktor-client-apache5:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("no.nav.helsearbeidsgiver:utils:$hagUtilsVersion")
    implementation("no.nav.security:token-client-core:$tokenClientCoreVersion")
    implementation("no.nav.security:token-validation-ktor-v2:$tokenClientCoreVersion")

    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(testFixtures("no.nav.helsearbeidsgiver:utils:$hagUtilsVersion"))
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        mavenNav("helsearbeidsgiver-${rootProject.name}")
    }
}

fun RepositoryHandler.mavenNav(repo: String): MavenArtifactRepository {
    val githubPassword: String by project

    return maven {
        setUrl("https://maven.pkg.github.com/navikt/$repo")
        credentials {
            username = "x-access-token"
            password = githubPassword
        }
    }
}
