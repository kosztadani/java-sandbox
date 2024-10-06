plugins {
    id("dev.kosztadani.sandbox.application")
}

dependencies {
    implementation(project(":tcp-example:common"))
    implementation(project(":tcp-example:server"))
    implementation(project(":tcp-example:client"))
}
