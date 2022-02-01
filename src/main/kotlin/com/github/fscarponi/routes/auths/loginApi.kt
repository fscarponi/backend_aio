package com.github.fscarponi.routes.auths

import Role
import com.github.fscarponi.routes.instance
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable


fun Route.loginApi() = route("/login") {
//    val jwtGenerator: AuthTokenGenerator by di().instance<AuthTokenGenerator>()
    post {
        val loginData = call.receive<LoginCredential>()
        val credential = loginData.username to loginData.password
        //todo go on db and validate username and hash password
        //and respond with token
        val jwt: AuthTokenGenerator by instance()
        call.respond(HttpStatusCode.OK, jwt.generateToken(credential.first, Role.BASE_USER))

    }


}



@Serializable
data class LoginCredential(val username: String, val password: String)
