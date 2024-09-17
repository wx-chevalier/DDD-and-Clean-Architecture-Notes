dependencies {
    implementation(project(":common"))
    implementation(project(":security:security-config"))
    implementation(project(":courses:courses-integration-events"))
    implementation(project(":administration:administration-integration-events"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    compileOnly("org.projectlombok:lombok:${Versions.lombok}")
    annotationProcessor("org.projectlombok:lombok:${Versions.lombok}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito:mockito-junit-jupiter:${Versions.mockito}")
    testImplementation("org.assertj:assertj-core:${Versions.assertj}")
}

tasks.test {
    useJUnitPlatform()
}
