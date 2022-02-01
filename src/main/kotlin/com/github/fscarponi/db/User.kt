package com.github.fscarponi.db

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class User(
    val mail: String,
    val hashPassword: String,
    @Serializable(with = InstantSerializer::class)
    val creationInstant: Instant? = Instant.now(),
    @Serializable(with = InstantSerializer::class)
    val lastPasswordModificationInstant: Instant? = null,
    val isBanned: Boolean = false,
    val verificationToken: String
)



