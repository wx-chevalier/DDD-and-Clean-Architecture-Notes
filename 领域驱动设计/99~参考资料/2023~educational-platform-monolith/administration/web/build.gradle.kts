dependencies {
    implementation(project(":common"))
    implementation(project(":web"))
    implementation(project(":security:security-config"))
    implementation(project(":administration:administration-application"))

    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-validation")

    compileOnly("org.projectlombok", "lombok", Versions.lombok)
    annotationProcessor("org.projectlombok", "lombok", Versions.lombok)

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.mockito", "mockito-junit-jupiter", Versions.mockito)
    testImplementation("org.assertj", "assertj-core", Versions.assertj)
    testImplementation("io.rest-assured", "rest-assured", Versions.restAssured)
    testImplementation("io.rest-assured", "json-path", Versions.restAssured)
    testImplementation("io.rest-assured", "xml-path", Versions.restAssured)

    testCompileOnly("org.projectlombok", "lombok", Versions.lombok)
    testAnnotationProcessor("org.projectlombok", "lombok", Versions.lombok)
}

tasks.test {
    useJUnitPlatform()
}
