package com.github.fscarponi.di

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.github.fscarponi.routes.auths.AIOTokenGenerator
import com.github.fscarponi.routes.auths.AuthTokenGenerator
import com.github.fscarponi.routes.auths.PasswordDigester
import com.github.fscarponi.routes.auths.SHA256Digester
import com.github.fscarponi.routes.prependIfMissing
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.io.File
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

object DIModules {

    val environment
        get() = DI.Module("environment") {
            environmentString("MONGO_URL"/*, default = "localhost:27017"*/) { it.prependIfMissing("mongodb://") }
            environmentString("JWT_AUDIENCE", default = "issuer")
            environmentString("JWT_REALM", default = "realm")
            environmentString("JWT_DOMAIN", default = "domain")
            environmentString("JWT_ISSUER", default = "issuer")
            environmentString("JWT_SECRET", default = "efawhuaefhui76!!@wefhuiah4twuifawenioefawhjiohu")

        }


    val security
        get() = DI.Module("security") {

            bind<JWTVerifier>() with singleton {
                JWT.require(instance())
                    .withAudience(instance("JWT_AUDIENCE"))
                    .withIssuer(instance("JWT_ISSUER"))
                    .build()
            }

            bind<AuthTokenGenerator>() with singleton {
                AIOTokenGenerator(instance("JWT_SECRET"), instance("JWT_ISSUER"))
            }

            bind<PasswordDigester>() with singleton { SHA256Digester() }

        }

}

