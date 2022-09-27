import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.helsearbeidsgiver"
version = "0.2.5"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jmailen.kotlinter")
    id("maven-publish")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    test {
        useJUnitPlatform()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion: String by project
    val mockkVersion: String by project
    val kotlinSerializationVersion: String by project
    val coroutinesVersion: String by project
    val junitJupiterVersion: String by project
    val tokenClientCoreVersion: String by project
    val nimbusJoseJwtVersion: String by project
    val logbackVersion: String by project
    val slf4jVersion: String by project

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")

    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("no.nav.security:token-client-core:$tokenClientCoreVersion")
    implementation("no.nav.security:token-validation-ktor-v2:$tokenClientCoreVersion")
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusJoseJwtVersion")

    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
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
