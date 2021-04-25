import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath(Libs.kotlinStdlib)
    }
}

plugins {
    kotlin("jvm") version Vers.kotlin apply true
    id("org.jetbrains.kotlin.plugin.noarg") version Vers.kotlin apply true
    id("org.springframework.boot") version Vers.springBoot apply true
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version Vers.kotlin
}

subprojects {
    group = "ru.phoenigm.idempotency.curator"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.noarg")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://repo.spring.io/milestone/")
        maven(url = "http://oss.jfrog.org/artifactory/oss-snapshot-local/")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.allWarningsAsErrors = true
        }

        withType<Wrapper> {
            gradleVersion = "6.8.1"
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
