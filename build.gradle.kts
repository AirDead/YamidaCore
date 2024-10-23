plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
}

group = "dev.airdead"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "ndkRepoReleases"
        url = uri("https://repo.nikdekur.tech/releases")
    }
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.kaml)
    implementation(libs.ndkore)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization)
    implementation(libs.invui)
    implementation(libs.invui.kotlin)
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
    }
}
tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
    }
}