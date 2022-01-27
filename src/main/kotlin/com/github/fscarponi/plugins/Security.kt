package com.github.fscarponi.plugins

import io.ktor.auth.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.apache.http.impl.auth.BasicScheme.authenticate
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureSecurity() {

    val audience=closestDI().direct.instance<String>("jwt.audience")
    val appRealm=closestDI().direct.instance<String>("jwt.realm")
    val domain=closestDI().direct.instance<String>("jwt.domain")

    authentication {
        jwt {
            realm = appRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256("hereMySuperSecret"))
                    .withAudience(audience)
                    .withIssuer(domain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
//    OAUTH N2B CONFIGURED
//    install(Authentication) {
//        oauth("auth-oauth-google") {
//            urlProvider = { "http://localhost:8080/callback" }
//            providerLookup = {
//                OAuthServerSettings.OAuth2ServerSettings(
//                    name = "google",
//                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
//                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
//                    requestMethod = HttpMethod.Post,
//                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
//                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
//                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
//                )
//            }
//            client = HttpClient(Apache)
//        }
//    }
//
//    routing {
//        authenticate("auth-oauth-google") {
//            get("login") {
//                call.respondRedirect("/callback")
//            }
//
//            get("/callback") {
//                val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
//                call.sessions.set(UserSession(principal?.accessToken.toString()))
//                call.respondRedirect("/hello")
//            }
//        }
//    }
}

class UserSession(accessToken: String)
