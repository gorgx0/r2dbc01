package com.gorg

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.*
import reactor.core.publisher.Mono
import reactor.core.publisher.SignalType

class Main {
}

private const val SELECT_SQL = "select * from table01"

fun main() {
    val connectionFactoryOptions = ConnectionFactoryOptions.builder()
        .option(DRIVER, "postgresql")
        .option(HOST, "localhost")
        .option(PORT, 5432)
        .option(USER, "r2dbc")
        .option(PASSWORD, "r2dbc")
        .option(DATABASE, "r2dbc")
        .build()

    val connectionFactory: ConnectionFactory = ConnectionFactories.get(connectionFactoryOptions)

    val res = Mono.from(connectionFactory.create()).block()

    println(res)
}

fun close(c: Connection?, st: SignalType?) {
    println(st.toString())
    c?.close()
}
