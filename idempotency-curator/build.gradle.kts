

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinCoroutines)
    implementation(Libs.kotlinLogging)
    api("com.hazelcast:hazelcast-spring:4.2")
    api("org.springframework.boot:spring-boot-starter-data-redis:2.4.5")

    runtimeOnly(Libs.jaxb)

    implementation(Libs.jacksonKotlin)

    implementation(Libs.springWeb)
}
