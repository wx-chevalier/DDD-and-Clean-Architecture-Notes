plugins {
    id("io.spring.dependency-management") version Versions.springDependencyManagementPlugin
    java
}

allprojects {
    group = "com.educational.platform"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("io.spring.dependency-management")
        plugin("java-library")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${Versions.spring}")
        }
    }
}

