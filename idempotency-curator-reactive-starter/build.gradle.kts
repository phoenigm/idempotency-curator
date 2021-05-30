plugins {
    kotlin("kapt") apply true
}

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinJdk8)
    implementation(Libs.kotlinLogging)

    implementation(Libs.springBootStarter)
    implementation(Libs.springBootDependencies)
    implementation(Libs.hazelcast)
    implementation(Libs.springBootRedis)

    api(project(":idempotency-curator-core"))
    implementation(project(":idempotency-curator-reactive"))
}
