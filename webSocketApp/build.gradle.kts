import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "br.com.websocket"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val cloud_aws_spring_version: String by project
val aws_version: String by project

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2021.0.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("io.awspring.cloud:spring-cloud-aws-context:$cloud_aws_spring_version")
	implementation("io.awspring.cloud:spring-cloud-aws-autoconfigure:$cloud_aws_spring_version")
	implementation("io.awspring.cloud:spring-cloud-aws-messaging:$cloud_aws_spring_version")
	implementation("com.amazonaws:aws-java-sdk-bom:$aws_version")

	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
