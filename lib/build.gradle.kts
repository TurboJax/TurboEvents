plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "org.turbojax"
version = "1.0.1"

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withJavadocJar()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "events"

            from(components["java"])

            pom {
                name = "TurboEvents"
                description = "A lightweight event library for turbo's mods"
                url = "https://github.com/turbojax/turbo-events"
                licenses {
                    license {
                        name = "Custom"
                        url = "https://github.com/turbojax/turbo-events/blob/main/LICENSE"
                    }
                }
                developers {
                    developer {
                        id = "turbojax"
                        name = "TurboJax"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/turbojax/turbo-events.git"
                    url = "https://github.com/turbojax/turbo-events"
                }
            }
        }
    }

    repositories {
        maven("https://maven.turbojax.org/releases") {
            credentials {
                username = project.findProperty("mavenUser") as String? ?: System.getenv("MAVEN_USERNAME")
                password = project.findProperty("mavenPass") as String? ?: System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}
