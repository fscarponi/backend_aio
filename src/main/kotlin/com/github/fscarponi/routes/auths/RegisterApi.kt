package com.github.fscarponi.routes.auths

import com.github.fscarponi.db.User
import com.github.fscarponi.db.databaseTransaction
import com.github.fscarponi.routes.instance
import com.mongodb.client.model.Filters
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import org.litote.kmongo.eq
import java.time.Instant
import java.util.*

fun Route.signupApi() = route("signup") {
    val passwordDigester: PasswordDigester by instance()
    post("register") {
        val reg = call.receive<UserRegistrationDataRequest>()
        databaseTransaction {
            if (usersCollection.findOne(User::mail eq reg.mail) != null) {
                call.respond(HttpStatusCode.Conflict)
            } else {
                usersCollection.insertOne(
                    User(
                        mail = reg.mail,
                        hashPassword = passwordDigester(reg.password),
                        creationInstant = Instant.now(),
                        verificationToken = UUID.randomUUID().toString()
                    )
                )
                //TODO we should now send this token to mail for activation (but we need a route for w8 this)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    get<VerifyRegistrationTokenLocation> { (emailBase64, token) ->
        val mail = Base64.getDecoder().decode(emailBase64).toString()
        val filter = Filters.and(
            User::mail eq mail,
            User::verificationToken eq token
        )
        databaseTransaction {
            val user = usersCollection.findOne(filter)
            user?.let {
                usersCollection.replaceOneById(user.id, user.copy(isVerified = true, verificationToken = null))
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.BadRequest)
        }


    }

}

@Location("verify/{emailBase64}/{token}")
data class VerifyRegistrationTokenLocation(val emailBase64: String, val token: String)

@Serializable
class UserRegistrationDataRequest(
    val mail: String, val password: String
)
