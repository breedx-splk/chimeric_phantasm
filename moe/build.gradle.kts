plugins {
    java
    war
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
}

tasks.war {
    archiveBaseName.set("moe")
    archiveVersion.set("")
}
