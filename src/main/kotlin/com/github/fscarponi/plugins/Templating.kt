package com.github.fscarponi.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

//
//fun Application.configureTemplating() {
//
//
//    routing {
//        get("/html-dsl") {
//            call.respondHtml {
//                body {
//                    h1 { +"HTML" }
//                    ul {
//                        for (n in 1..10) {
//                            li { +"$n" }
//                        }
//                    }
//                }
//            }
//        }
//        get("/styles.css") {
//            call.respondCss {
//                body {
//                    backgroundColor = Color.darkBlue
//                    margin(0.px)
//                }
//                rule("h1.page-title") {
//                    color = Color.white
//                }
//            }
//        }
//
//        get("/html-css-dsl") {
//            call.respondHtml {
//                head {
//                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
//                }
//                body {
//                    h1(classes = "page-title") {
//                        +"Hello from Ktor!"
//                    }
//                }
//            }
//        }
//    }
//}
//
//suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
//    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
//}
//
