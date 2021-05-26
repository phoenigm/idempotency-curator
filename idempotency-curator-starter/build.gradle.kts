plugins {
    kotlin("kapt") apply true
}

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinLogging)

    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.springframework.boot:spring-boot-starter:${Vers.springBoot}")

    implementation("org.springframework.boot:spring-boot-dependencies:${Vers.springBoot}")
    api(project(":idempotency-curator"))

}
