project.description =
    "A library for enforcing architectural standards within Spring Modulith modules using ArchUnit, with a focus on hexagonal architecture."

dependencies {
    implementation("com.tngtech.archunit:archunit:${property("archUnitVersion")}")
    implementation("org.springframework.modulith:spring-modulith-core")

    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
}