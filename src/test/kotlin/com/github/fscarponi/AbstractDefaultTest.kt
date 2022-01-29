package com.github.fscarponi

import com.github.fscarponi.di.DIModules


import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.SerializersModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance

import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractDefaultTest : DIAware {

    override val di = DI.lazy {
        import(DIModules.environment)
        import(DIModules.security)
    }


    @BeforeEach
    fun init(): Unit = runBlocking {



    }

    @AfterAll
    fun end(): Unit = runBlocking {

    }


}
