dependencies {
    implementation(project(":common"))
    implementation("org.springframework", "spring-context")

    compileOnly("org.projectlombok", "lombok", Versions.lombok)
    annotationProcessor("org.projectlombok", "lombok", Versions.lombok)
}