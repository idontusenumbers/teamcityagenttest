plugins {
    id("java")
}

group = "net.obive"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    val testContainersVersion = "1.20.4"
    val log4jVersion = "2.24.1"
    
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")


    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.slf4j:slf4j-simple:2.0.16")
//    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
//    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
}

tasks.test {
    useJUnitPlatform()

    testLogging.showStandardStreams = true

    testLogging {
        events("PASSED", "SKIPPED", "FAILED", "STANDARD_OUT", "STANDARD_ERROR")
    }
}
