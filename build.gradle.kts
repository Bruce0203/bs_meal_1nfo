val kotlin_version = "1.6.10"
plugins {
    application
    kotlin("jvm") version "1.6.10"
}

application {
    mainClass.set("io.github.bruce0203.bsmeal1nfo.AppKt")
}

repositories {
    mavenCentral()

}

dependencies {
    implementation("com.github.instagram4j:instagram4j:2.0.7")
    //https://github.com/agemor/neis-api/releases/tag/4.0.1
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
}