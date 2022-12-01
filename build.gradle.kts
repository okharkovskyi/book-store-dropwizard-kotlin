import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.7.20"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.dropwizard:dropwizard-core:2.1.4")
    implementation("io.dropwizard:dropwizard-db:2.1.4")
    implementation("io.dropwizard.modules:dropwizard-flyway:2.1.0-1")
    implementation("ru.vyarus:dropwizard-guicey:5.6.1")
    implementation("ru.vyarus.guicey:guicey-eventbus:5.6.1-1")
    implementation("org.jooq:jooq:3.14.0")
    implementation("org.postgresql:postgresql:42.5.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    withType<ShadowJar> {
        isZip64 = true
        mergeServiceFiles()
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
        exclude("META-INF/*.DSA", "META-INF/*.RSA")
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

val build: DefaultTask by tasks
val shadowJar = tasks["shadowJar"] as ShadowJar
build.dependsOn(shadowJar)

application {
    mainClass.set("com.example.bookstore.BookstoreApp")
}

