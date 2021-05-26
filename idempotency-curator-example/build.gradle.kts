apply {
    plugin("org.springframework.boot")
}

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinCoroutines)
    implementation(Libs.kotlinLogging)

    implementation(Libs.springWeb)
    implementation(project(":idempotency-curator-starter"))

    testImplementation(Libs.springTest) {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}
