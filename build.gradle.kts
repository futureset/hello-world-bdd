import com.diffplug.gradle.spotless.SpotlessExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    `java`
    `jacoco`
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot)
}

extensions.configure<JacocoPluginExtension> {
    toolVersion = libs.versions.jacoco.get()
}

group = "net.futureset"
version = "local-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

val allTestImplementation: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = false
    isVisible = false
}

dependencies {

    allTestImplementation(enforcedPlatform(libs.mockito.bom))
    allTestImplementation(enforcedPlatform(libs.junit.bom))
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(libs.spring.boot.starter)
    allTestImplementation(libs.bundles.junit5) {
        exclude(group = "org.hamcrest")
    }
}

val verGradle: String by project

tasks.wrapper {
    gradleVersion = verGradle
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                implementation(libs.junit.mockito)
                implementation(libs.assertj)
            }
            targets {
                all {
                    testTask.configure {
                        finalizedBy("jacocoTestReport")
                    }
                }
            }
        }

        register<JvmTestSuite>("integrationTest") {
            testType.set(TestSuiteType.INTEGRATION_TEST)
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                implementation(project())
                implementation.bundle(libs.bundles.cucumber) {
                    exclude(group = "junit", module = "junit")
                    exclude(group = "org.hamcrest")
                }
                implementation(libs.spring.boot.starter.test) {
                    exclude(group = "org.hamcrest")
                    exclude(group = "junit", module = "junit")
                }
                implementation(libs.junit.vintage)
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        finalizedBy("jacocoIntegrationTestReport")
                    }
                }
            }
        }
    }
}

val unitTestCoverageLimit: String by project

val jacocoTestCoverageVerification by tasks.existing(JacocoCoverageVerification::class) {
    executionData(tasks.test.get())
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = BigDecimal(unitTestCoverageLimit).divide(BigDecimal.valueOf(100)).setScale(2)
            }
        }
    }
}
val integrationTest by tasks.existing

val jacocoIntegrationTestReport by tasks.registering(JacocoReport::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    executionData(integrationTest.get())
    sourceSets(project.sourceSets["main"])
    reports {
        html.outputLocation.set(jacoco.reportsDirectory.map { it.dir("integration/html") })
    }
}

val integrationTestCoverageLimit: String by project

val jacocoIntegrationTestCoverageVerification by tasks.registering(JacocoCoverageVerification::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    executionData(integrationTest.get())
    dependsOn(integrationTest)
    violationRules {
        rule {
            limit {
                minimum = BigDecimal(integrationTestCoverageLimit).divide(BigDecimal.valueOf(100)).setScale(2)
            }
        }
    }
}

configurations.matching { name.lowercase().endsWith("testimplementation") }.configureEach {
    extendsFrom(allTestImplementation)
}

tasks.named("check") {
    dependsOn("jacocoTestCoverageVerification")
    dependsOn("jacocoIntegrationTestCoverageVerification")
}

extensions.configure<SpotlessExtension> {
    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", "*.md", ".gitignore", "**/*.feature")

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
    java {
        palantirJavaFormat()
        target("src/**/*.java")
    }
}

tasks.processResources {
    dependsOn("spotlessApply")
}
