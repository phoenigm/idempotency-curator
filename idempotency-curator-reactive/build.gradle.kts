dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinLogging)

    implementation(Libs.springWebflux)

    implementation(project(":idempotency-curator-core"))
}
