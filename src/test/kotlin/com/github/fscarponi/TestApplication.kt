package com.github.fscarponi


import com.github.fscarponi.routes.auths.authenticatedWs
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking


fun <R> withServer(test: suspend TestApplicationEngine.() -> R) =
    withTestApplication(moduleFunction = {
        managerModule()
        routing {
            trace {
                application.log.debug(it.buildText())
            }
            //OLNY FOR TESTS
            authenticatedWs(Role.ADMIN, "api/test/authWSmanager") {
                while (true) {
                    outgoing.send(Frame.Text((incoming.receive() as Frame.Text).readText()))
                    application.log.info("SERVERSIDE AWS->echo done")
                }

            }
        }
        Thread.sleep(2000)
    }, test = test)


//per lanciare i test come suspending
fun <R> withApplication(
    environment: ApplicationEngineEnvironment = createTestEnvironment(),
    configure: TestApplicationEngine.Configuration.() -> Unit = {},
    test: suspend TestApplicationEngine.() -> R
): R {
    val engine = TestApplicationEngine(environment, configure)
    engine.start()
    try {
        return runBlocking {
            engine.test()
        }
    } finally {
        engine.stop(0L, 0L)
    }
}

fun <R> withTestApplication(moduleFunction: Application.() -> Unit, test: suspend TestApplicationEngine.() -> R): R {
    return withApplication(createTestEnvironment()) {
        moduleFunction(application)
        test()
    }
}

fun TestApplicationRequest.addHeader(name: String, value: ContentType) =
    addHeader(name, value.toString())

fun TestApplicationEngine.defaultRequest(
    method: HttpMethod,
    uri: String,
    body: String?=null,
    action: TestApplicationRequest.() -> Unit = {}
) = handleRequest(method, uri) {
    body?.let {
        setBody(it)
    }
    addHeader(HttpHeaders.ContentType, ContentType.Application.Json)
    action()
}

