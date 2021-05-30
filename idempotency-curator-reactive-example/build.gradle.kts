apply {
    plugin("org.springframework.boot")
}

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinCoroutines)
    implementation(Libs.kotlinLogging)

    implementation(Libs.springWebflux)
    implementation(Libs.jacksonKotlin)
    implementation(project(":idempotency-curator-reactive-starter"))

    testImplementation(Libs.springTest) {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}
