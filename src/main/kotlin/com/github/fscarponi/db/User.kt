package com.github.fscarponi.db

import java.time.Instant

data class User(
    val userName: String,
    val mail: String,
    val hashPassword: String,
    val activationDate: Instant? = null,
    val lastPasswordModification: Instant,
    val isBanned: Boolean = false
)
