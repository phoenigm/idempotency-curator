dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinLogging)

    implementation(Libs.springWeb)

    implementation(project(":idempotency-curator-core"))
}
