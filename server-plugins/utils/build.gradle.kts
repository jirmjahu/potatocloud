group = "net.potatocloud.plugins.utils"

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    compileOnly(libs.minimessage)
    implementation(libs.simpleyaml)
}
