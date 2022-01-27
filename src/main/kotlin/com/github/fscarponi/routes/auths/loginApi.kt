package com.github.fscarponi.routes.auths

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*



fun Route.loginApi() = route("auth") {
//    val jwtGenerator: AuthTokenGenerator by di().instance<AuthTokenGenerator>()
    post("/login") {
        val logindata = call.receive<LoginCredential>()
        val credential = logindata.username to logindata.password
        //todo go on db and validate username and hash password
        //and respond with token
        call.respond(HttpStatusCode.OK)


    }

    post { }


}

data class LoginCredential (val username: String, val password : String)
