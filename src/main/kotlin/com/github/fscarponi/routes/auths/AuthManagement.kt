package com.github.fscarponi.routes.auths


import Role
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.github.fscarponi.routes.BasePrincipal
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.instance
import java.security.MessageDigest
import java.time.Instant
import java.util.*
import kotlin.time.ExperimentalTime

interface AuthTokenGenerator {
    fun generateToken(subject: String, role: Role): AuthTokenResponseData
}

data class AIOTokenGenerator(
    private val algorithm: Algorithm,
    private val issuer: String,
    private val audience: String = issuer
) : AuthTokenGenerator {

    @OptIn(ExperimentalTime::class)
    override fun generateToken(subject: String, role: Role): AuthTokenResponseData {
        val now = Instant.now()
//        val expAtDate =  now.plusSeconds(60).toEpochMilli()//ONLY FOR TEST
        val expAtDate = Date(
            when (role) {
                Role.ADMIN -> now.plusSeconds(3600 * 24).toEpochMilli()
                else -> now.plusSeconds(3600 * 1).toEpochMilli()
            }
        )
        val jwt = JWT.create()
            .withIssuer(issuer)
            .withAudience(issuer)
            .withExpiresAt(expAtDate)
            .withSubject(subject)
            .withClaim("createdAt", now.toEpochMilli())
            .withClaim("role", role.toString())
            .sign(algorithm)
        return AuthTokenResponseData(jwt, expAtDate.time)
    }

}


@Serializable
data class AuthTokenResponseData(val jwt: String, val expAt: Long)


interface PasswordDigester {

    fun digest(password: String): String

    operator fun invoke(password: String) =
        digest(password)

}


class SHA256Digester : PasswordDigester {
    override fun digest(password: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("") { str, byte ->
                str + "%02x".format(byte)
            }
}


typealias AuthenticatedWebSocketServerSession = suspend DefaultWebSocketServerSession.(BasePrincipal) -> Unit

fun Route.authenticatedWs(
    role: Role,
    path: String = "",
    handler: AuthenticatedWebSocketServerSession
) = webSocket(path = path) {
    val verifier: JWTVerifier by DI.direct {} .di.instance()//TODO NOT RIGHT INVOCATION FOR DI application.di do not build as expected
    val jwt: String by call.parameters
    val decodedJwt = verifier.verify(jwt)!!
    val claimRole = decodedJwt.claims["role"]?.let { Role.valueOf(it.asString().toUpperCase()) }
    try {
        require(decodedJwt.expiresAt.toInstant() >= Instant.now()) { "JWT expired" }
        require(claimRole != null) { "No role found in claim \"role\"" }
        require(role <= claimRole) { "Unauthorized. Clearance too low." }
    } catch (e: Exception) {
        application.log.error("Closing websocket", e)
        close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Violated Policy"))
        return@webSocket
    }
    handler(BasePrincipal(decodedJwt.subject!!, claimRole))
}
