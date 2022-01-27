package com.github.fscarponi

import com.github.fscarponi.di.DIModules
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import com.github.fscarponi.plugins.*
import io.ktor.application.*
import io.ktor.locations.*
import org.kodein.di.ktor.DIFeature

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        install(DIFeature) {
            import(DIModules.security)
            import(DIModules.environment)
        }
        install(Locations) {
        }
        configureRouting()
        configureSecurity()
        configureHTTP()
        configureMonitoring()
        configureSerialization()
        configureSockets()
        configureAdministration()
    }.start(wait = true)
}
