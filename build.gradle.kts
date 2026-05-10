plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    war
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}
val springAiVersion by extra("2.0.0-M5")
val springCloudVersion by extra("2025.1.1")

group = "com.chatting"
version = "0.0.1-SNAPSHOT"
description = "chatUp"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    annotationProcessor("org.projectlombok:lombok")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat-runtime")
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("org.assertj:assertj-core:")
    testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.2.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

//jte {
//    generate()
//    binaryStaticContent = true
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
