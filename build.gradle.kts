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
    kotlin("jvm") version Vers.kotlin apply false
    id("org.springframework.boot") version Vers.springBoot
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version Vers.kotlin
}

subprojects {
    group = "ru.phoenigm.idempotency.curator"

    repositories {
        mavenCentral()
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
