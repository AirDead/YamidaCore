plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    implementation("com.charleskorn.kaml:kaml:0.61.0")
    implementation("dev.nikdekur:ndkore:1.4.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")
}


kotlin {
    jvmToolchain(21)
}
