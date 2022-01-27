package com.github.fscarponi.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun DI.Builder.environmentString(
    tag: String,
    name: String = tag,
    default: String? = null,
    transform: ((String) -> String)? = null
) =
    bind<String>(tag) with singleton {
        (System.getenv(name) ?: default)?.let { if (transform != null) transform(it) else it }
            ?: error("Cannot find $name in environment")
    }

fun DI.Builder.environmentBoolean(
    tag: String, name: String = tag,
    default: Boolean? = null,
    transform: ((Boolean) -> Boolean)? = null
) =
    bind<Boolean>(tag) with singleton {
        (System.getenv(name)?.toBoolean() ?: default)?.let { if (transform != null) transform(it) else it }
            ?: error("Cannot find $name in environment")
    }

fun DI.Builder.environmentInt(
    tag: String,
    name: String = tag,
    default: Int? = null,
    transform: ((Int) -> Int)? = null
) =
    bind<Int>(tag) with singleton {
        (System.getenv(name)?.toInt() ?: default)?.let { if (transform != null) transform(it) else it }
            ?: error("Cannot find $name in environment")
    }
