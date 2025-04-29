plugins {
    alias(projLibs.plugins.kotlin.jvm)
    application
}

group = "com.promikhail.research.ip4list.generator"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(projLibs.versions.java.version.get().toInt())
}

dependencies {
    implementation(project(":tools"))

    implementation(projLibs.kotlinx.coroutines.core.jvm)

    testImplementation(projLibs.kotlin.test)
    testImplementation(projLibs.kotlinx.coroutines.test)
}

application {
    mainClass = "com.promikhail.research.ip4list.generator.MainKt"
}