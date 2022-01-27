package com.github.fscarponi.di

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.github.fscarponi.routes.auths.AIOTokenGenerator
import com.github.fscarponi.routes.auths.AuthTokenGenerator
import com.github.fscarponi.routes.auths.PasswordDigester
import com.github.fscarponi.routes.auths.SHA256Digester
import com.github.fscarponi.routes.prependIfMissing
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object DIModules {

    val environment
        get() = DI.Module("environment") {
            environmentString("MONGO_URL"/*, default = "localhost:27017"*/) { it.prependIfMissing("mongodb://") }
            environmentString("jwt.audience", default = "audience")
            environmentString("jwt.realm", default = "realm")
            environmentString("jwt.domain", default = "domain")

        }


    val security
        get() = DI.Module("security") {

            bind<String>("JWT_AUDIENCE") with singleton {
                System.getenv("JWT_ISSUER") ?: instance("JWT_ISSUER")
            }

            bind<JWTVerifier>() with singleton {
                JWT.require(instance())
                    .withAudience(instance("JWT_AUDIENCE"))
                    .withIssuer(instance("JWT_ISSUER"))
                    .build()
            }

            bind<Algorithm>() with singleton { Algorithm.RSA256(instance(), instance()) }

            bind<AuthTokenGenerator>() with singleton {
                AIOTokenGenerator(algorithm = instance(), instance("JWT_ISSUER"))
            }

            bind<PasswordDigester>() with singleton { SHA256Digester() }

        }

}
