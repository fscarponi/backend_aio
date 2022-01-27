package com.github.fscarponi

import com.auth0.jwt.interfaces.JWTVerifier
import com.github.fscarponi.di.DIModules
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import com.github.fscarponi.plugins.*
import com.github.fscarponi.routes.auths.loginApi
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.ktor.DIFeature
import org.kodein.di.ktor.di
import org.slf4j.event.Level

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        managerModule()
    }.start(wait = true)
}


fun Application.managerModule() {

    install(Locations)

    install(DIFeature) {
        import(DIModules.environment)
        import(DIModules.security)
    }

    configureRouting()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureAdministration()


}
