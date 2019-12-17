val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val hikari_version: String by project
val mysql_version: String by project

val docker_id = "adetalhouet"

plugins {
    application
    kotlin("jvm") version "1.3.60"

    id("com.google.cloud.tools.jib") version "1.6.1"

    id("org.flywaydb.flyway") version "6.1.1"

    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "io.adetalhouet.directory"
version = "1.0.0"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")

    implementation("com.h2database", "h2", "1.4.199")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    implementation("mysql:mysql-connector-java:$mysql_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

jib {
    from {
        image = "openjdk:8-jre-alpine"
    }
    to {
        val tag_version = version.toString().substringBefore('-')
        image = "$docker_id/${project.name}:$tag_version"
    }
    container {
        mainClass = application.mainClassName
        ports = listOf("8080")
    }
}

tasks.jibDockerBuild {
    dependsOn(tasks.build)
}

flyway {
    url = System.getenv("DB_HOST")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    baselineOnMigrate = true
}