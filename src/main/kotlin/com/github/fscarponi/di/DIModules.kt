package com.github.fscarponi.di

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.github.fscarponi.db.TransactionContext
import com.github.fscarponi.routes.auths.AIOTokenGenerator
import com.github.fscarponi.routes.auths.AuthTokenGenerator
import com.github.fscarponi.routes.auths.PasswordDigester
import com.github.fscarponi.routes.auths.SHA256Digester
import com.github.fscarponi.routes.prependIfMissing
import org.kodein.di.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine

object DIModules {

    val environment
        get() = DI.Module("environment") {
            environmentString("MONGO_URL", default = "localhost:27017") { it.prependIfMissing("mongodb://") }
            environmentString("JWT_AUDIENCE", default = "issuer")
            environmentString("JWT_REALM", default = "realm")
            environmentString("JWT_DOMAIN", default = "domain")
            environmentString("JWT_ISSUER", default = "issuer")
            environmentString("JWT_SECRET", default = "efawhuaefhui76!!@wefhuiah4twuifawenioefawhjiohu")
            environmentBoolean("PRODUCTION", default = false)

        }


    val database
        get() = DI.Module("database") {
            bind<String>("DATABASE_NAME") with singleton {
                if (instanceOrNull<Boolean>("PRODUCTION") == true) "aio-db" else "test"
            }
            bind<CoroutineDatabase>() with singleton {
                instance<com.mongodb.reactivestreams.client.MongoClient>().getDatabase(instance("DATABASE_NAME")).coroutine
            }
            bind<TransactionContext>() with provider {
                TransactionContext(di)
            }

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

