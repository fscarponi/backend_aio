package com.github.fscarponi.routes

import Role
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


data class BasePrincipal(val userId: String, val role: Role) : Principal


fun String.prependIfMissing(s: String) =
    if (startsWith(s)) this else s + this


typealias DefaultContext = PipelineContext<Unit, ApplicationCall>

inline fun <reified T : Any, reified K : Any> PipelineContext<Unit, ApplicationCall>.instance(
    tag: Any? = null,
    arg: T
) =
    closestDI().instance<T, K>(tag, arg)

inline fun <reified T : Any> DefaultContext.instance(tag: Any? = null) =
    closestDI().instance<T>(tag)

inline fun <reified T : Any> Application.instance(tag: Any? = null) =
    closestDI().instance<T>(tag)

inline fun <reified T : Any> Route.instance(arg: Any? = null) =
    closestDI().instance<T>(arg)
