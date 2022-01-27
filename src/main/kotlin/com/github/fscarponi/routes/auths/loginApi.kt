package com.github.fscarponi.routes.auths

import com.github.fscarponi.routes.instance
import io.ktor.application.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import io.ktor.util.reflect.*
import kotlinx.serialization.Serializable
import org.kodein.di.DIProperty
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import kotlin.properties.ReadOnlyProperty


fun Route.loginApi() = route("/login") {
//    val jwtGenerator: AuthTokenGenerator by di().instance<AuthTokenGenerator>()
    post() {
        val logindata = call.receive<LoginCredential>()
        val credential = logindata.username to logindata.password
        //todo go on db and validate username and hash password
        //and respond with token
        val jwt: AuthTokenGenerator by instance()
        call.respond(HttpStatusCode.OK,jwt.generateToken(credential.first,Role.BASE_USER))

    }


}



@Serializable
data class LoginCredential(val username: String, val password: String)
