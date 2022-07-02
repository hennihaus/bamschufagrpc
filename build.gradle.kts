import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.kotlinx.kover")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
    id("com.github.johnrengelman.shadow")
    id("com.google.protobuf")
}

group = "de.hennihaus"
version = "0.0.1"

application {
    mainClass.set("de.hennihaus.Application")
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "de.hennihaus.Application")
    }
}

sourceSets {
    main {
        java.srcDirs(
            "build/generated/ksp/main/kotlin",
        )
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    // exclude kotlin test libraries
    exclude("org.jetbrains.kotlin", "kotlin-test")
    exclude("org.jetbrains.kotlin", "kotlin-test-common")
    exclude("org.jetbrains.kotlin", "kotlin-test-annotations-common")
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}

dependencies {
    val kotlinVersion: String by project
    val ktorVersion: String by project
    val logbackVersion: String by project
    val kotestVersion: String by project
    val kotestLibariesVersion: String by project
    val mockkVersion: String by project
    val junitVersion: String by project
    val koinVersion: String by project
    val kotlinDateTimeVersion: String by project
    val koinAnnotationsVersion: String by project
    val konformVersion: String by project
    val grpcKotlinVersion: String by project
    val grpcJavaVersion: String by project
    val protobufCoreVersion: String by project
    val grpcNettyVersion: String by project

    // ktor common plugins
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // ktor client plugins
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-resources-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")

    // koin plugins
    implementation("io.insert-koin:koin-core:$koinVersion")
    compileOnly("io.insert-koin:koin-annotations:$koinAnnotationsVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
    ksp("io.insert-koin:koin-ksp-compiler:$koinAnnotationsVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")

    // grpc plugins
    implementation("io.grpc:grpc-netty:$grpcNettyVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcJavaVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufCoreVersion")

    // utility plugins
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:$kotlinDateTimeVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("io.konform:konform-jvm:$konformVersion")

    // test plugins
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-ktor-jvm:$kotestLibariesVersion")
    testImplementation("io.kotest:kotest-assertions-kotlinx-time-jvm:$kotestLibariesVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

protobuf {
    val grpcKotlinVersion: String by project
    val grpcJavaVersion: String by project
    val protobufCoreVersion: String by project

    generatedFilesBaseDir = "$buildDir/generated/proto"
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufCoreVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcJavaVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}

ktlint {
    ignoreFailures.set(false)
    filter {
        exclude("**/generated/**")
    }
}

detekt {
    config = files("config/detekt/detekt.yml")
    source = source.from(
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_JAVA,
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
        "src/integrationTest/java",
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
        "src/integrationTest/kotlin",
    )
}

tasks.init {
    dependsOn(tasks.ktlintApplyToIdea)
}

kover {
    coverageEngine.set(kotlinx.kover.api.CoverageEngine.INTELLIJ)
}

tasks.koverMergedVerify {
    rule {
        bound {
            val minTestCoverageInPercent: String by project
            minValue = minTestCoverageInPercent.toInt()
            valueType = kotlinx.kover.api.VerificationValueType.COVERED_LINES_PERCENTAGE
        }
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation(sourceSets.test.get().output)
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

tasks.check {
    dependsOn(testing.suites.named("integrationTest"))
}
