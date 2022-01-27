val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "com.github.fscarponi"
version = "0.0.1"
application {
    mainClass.set("com.github.fscarponi.ApplicationKt")
}
kotlin {
    sourceSets {
        all {
            languageSettings.optIn("io.ktor.locations.KtorExperimentalLocationsAPI")
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
            languageSettings.optIn("kotlin.ExperimentalStdlibApi")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    //DI
    implementation("org.kodein.di:kodein-di:7.10.0")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:7.10.0")


    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")



}


//
//publishing {
//    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/fscarponi/${rootProject.name}")
//            credentials {
//                username = "fscarponi"
//                password = searchPropertyOrNull("GITHUB_TOKEN")
//            }
//        }
//    }
//    publications {
//        create<MavenPublication>(project.name) {
//            from(components["kotlin"])
//            artifact(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar"))
//            groupId = project.group as String
//            artifactId = "${rootProject.name}-${project.name}"
//            version = project.version as String
//        }
//    }
//}
