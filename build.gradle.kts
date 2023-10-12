import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "no.nav.helsearbeidsgiver"
version = "0.3.0"

plugins {
    kotlin("jvm")
    id("org.jmailen.kotlinter")
    id("maven-publish")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
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
}

dependencies {
    val ktorVersion: String by project
    val mockkVersion: String by project
    val kotlinCoroutinesVersion: String by project
    val kotlinSerializationVersion: String by project
    val junitJupiterVersion: String by project
    val tokenClientCoreVersion: String by project
    val nimbusJoseJwtVersion: String by project
    val logbackVersion: String by project
    val slf4jVersion: String by project
    val jacksonVersion: String by project


    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusJoseJwtVersion")
    implementation("io.ktor:ktor-client-apache5:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("no.nav.security:token-client-core:$tokenClientCoreVersion")
    implementation("no.nav.security:token-validation-ktor-v2:$tokenClientCoreVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
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
