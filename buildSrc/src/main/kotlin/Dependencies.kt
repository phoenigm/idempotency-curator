object Vers {
    const val kotlin = "1.4.20"
    const val kotlinx = "1.4.3"

    const val springBoot = "2.4.2"

    const val kotlinLogging = "2.0.6"

    const val reactorTest = "3.4.2"
    const val jacksonKotlin = "2.12.1"
    const val jaxb = "2.3.1"
    const val swagger = "3.0.0"
    const val hazelcast = "4.2"
    const val servletApi = "4.0.1"
}

object Libs {
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Vers.kotlinx}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Vers.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Vers.kotlin}"
    const val kotlinJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Vers.kotlin}"
    const val kotlinLogging = "io.github.microutils:kotlin-logging-jvm:${Vers.kotlinLogging}"

    const val springWeb = "org.springframework.boot:spring-boot-starter-web:${Vers.springBoot}"
    const val springWebflux = "org.springframework.boot:spring-boot-starter-webflux:${Vers.springBoot}"
    const val springBootStarter = "org.springframework.boot:spring-boot-starter:${Vers.springBoot}"
    const val springBootDependencies = "org.springframework.boot:spring-boot-dependencies:${Vers.springBoot}"
    const val springConfigProcessor = "org.springframework.boot:spring-boot-configuration-processor:${Vers.springBoot}"
    const val springTest = "org.springframework.boot:spring-boot-starter-test:${Vers.springBoot}"
    const val springBootRedis = "org.springframework.boot:spring-boot-starter-data-redis:${Vers.springBoot}"

    const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Vers.jacksonKotlin}"
    const val servletApi = "javax.servlet:javax.servlet-api:${Vers.servletApi}"
    const val reactorTest = "io.projectreactor:reactor-test:${Vers.reactorTest}"

    const val jaxb = "javax.xml.bind:jaxb-api:${Vers.jaxb}"

    const val hazelcast = "com.hazelcast:hazelcast-spring:${Vers.hazelcast}"
}
