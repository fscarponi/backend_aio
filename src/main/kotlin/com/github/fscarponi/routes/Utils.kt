package com.github.fscarponi.routes

import Role
import io.ktor.auth.*


data class BasePrincipal(val userId: String, val role: Role) : Principal


fun String.prependIfMissing(s: String) =
    if (startsWith(s)) this else s + this


//inline fun <reified T : Any> Application.instance(tag: Any? = null) =
//    closestDI().instance<T>(tag)


