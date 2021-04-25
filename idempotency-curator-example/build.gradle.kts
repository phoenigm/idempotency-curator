
dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinCoroutines)
    implementation(Libs.kotlinLogging)
    implementation("com.hazelcast:hazelcast-spring:4.2")

    runtimeOnly(Libs.jaxb)

    implementation(Libs.swagger)
    implementation(Libs.swaggerUi)
    implementation(Libs.jacksonKotlin)

    implementation(Libs.springWeb)
    implementation(project(":idempotency-curator"))

    testImplementation(Libs.springTest) {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}
