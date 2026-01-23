import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
}

group = "net.potatocloud.plugin.platform.spigot.legacy"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":connector"))

    compileOnly(libs.spigot)
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("potatocloud-plugin-spigot-legacy")
    archiveVersion.set("")
    archiveClassifier.set("")
    relocate("io.netty", "net.potatocloud.shaded.netty")
}

