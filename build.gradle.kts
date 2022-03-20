import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20-RC"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.gorg.r2dbc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:2020.0.17")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.r2dbc:r2dbc-spi:0.9.1.RELEASE")
//    implementation("io.r2dbc:r2dbc-spi-test:0.9.1.RELEASE")
    implementation("io.r2dbc:r2dbc-proxy:0.9.0.RELEASE")
//    implementation("io.r2dbc:r2dbc-pool:0.9.0.RELEASE")
//    implementation("io.r2dbc:r2dbc-postgresql:0.8.12.RELEASE")
    implementation("org.postgresql:postgresql:42.3.3")
    implementation("org.postgresql:r2dbc-postgresql:0.9.1.RELEASE")
    implementation("io.projectreactor:reactor-core")
    implementation("io.projectreactor:reactor-tools")
    implementation("org.hibernate:hibernate-core:5.6.5.Final")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}