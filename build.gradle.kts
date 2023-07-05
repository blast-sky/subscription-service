import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.unbroken-dome.test-sets") version "4.0.0"
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("kapt") version "1.8.22"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "com.astrog"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}


testSets {
	create("integrationTest")
}

val integrationTestImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.liquibase:liquibase-core")

	implementation("io.github.oshai:kotlin-logging-jvm:4.0.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	runtimeOnly("org.postgresql:postgresql")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("com.ninja-squad:springmockk:3.0.1")

	integrationTestImplementation("org.junit.jupiter:junit-jupiter")
	integrationTestImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.mockito", module = "mockito-core")
	}
	integrationTestImplementation("org.springframework.amqp:spring-rabbit-test")
	integrationTestImplementation("com.ninja-squad:springmockk:3.0.1")
	// integrationTestImplementation("com.h2database:h2")
	integrationTestImplementation("org.springframework.boot:spring-boot-testcontainers")
	integrationTestImplementation("org.testcontainers:junit-jupiter")
	integrationTestImplementation("org.testcontainers:postgresql")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
