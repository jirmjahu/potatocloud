import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
}

group = "net.potatocloud.plugins.labymod"

repositories {
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://dist.labymod.net/api/v1/maven/release/")
}

dependencies {
    compileOnly(project(":api"))
    implementation(project(":plugins:utils"))
    implementation(libs.simpleyaml)
    implementation(libs.labymod.common)
    implementation(libs.labymod.bukkit)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.spigot)
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("potatocloud-plugin-labymod")
    archiveVersion.set("${rootProject.version}")
    archiveClassifier.set("")
}

