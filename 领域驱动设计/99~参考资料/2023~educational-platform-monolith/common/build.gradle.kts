dependencies {
    api("org.axonframework", "axon-spring-boot-starter", Versions.axon) {
        exclude(group = "org.axonframework", module = "axon-server-connector")
    }

    runtimeOnly("com.h2database", "h2")
}