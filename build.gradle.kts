import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.xerial:sqlite-jdbc:3.41.2.2") // SQLite JDBC Driver
}

tasks.test {
    useJUnitPlatform()
}

//Files to fix JVM-target:


tasks.withType<JavaCompile> {
    sourceCompatibility = "22"
    targetCompatibility = "22"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "22"
    }
}