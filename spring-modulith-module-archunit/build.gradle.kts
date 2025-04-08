dependencies {
    implementation("com.tngtech.archunit:archunit:${property("archUnitVersion")}")
    implementation("org.springframework.modulith:spring-modulith-core")

    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
}