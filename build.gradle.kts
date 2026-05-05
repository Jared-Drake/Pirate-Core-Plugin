plugins {
    java
}

group = "com.jared"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.10.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}