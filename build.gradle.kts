plugins {
    id("java")
    id("maven-publish")
    id("io.spring.dependency-management") version "1.1.7"
}

extra["springModulithVersion"] = "1.3.3"
extra["springBootVersion"] = "3.4.4"
extra["archUnitVersion"] = "1.4.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "io.spring.dependency-management")

    group = "dev.clutcher.modulith"
    version = "1.0.0"

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
            mavenBom("org.springframework.boot:spring-boot-dependencies:${property("springBootVersion")}")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}