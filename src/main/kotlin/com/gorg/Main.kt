package com.gorg

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions.*
import io.r2dbc.spi.Result
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.Objects
import java.util.function.Consumer

private const val SELECT_SQL = "select * from table01"

fun main() {
    val connectionFactoryOptions = builder()
        .option(DRIVER, "postgresql")
        .option(HOST, "localhost")
        .option(PORT, 5432)
        .option(USER, "r2dbc")
        .option(PASSWORD, "r2dbc")
        .option(DATABASE, "r2dbc")
        .build()

    val connectionFactory: ConnectionFactory = ConnectionFactories.get(connectionFactoryOptions)

    val res = (Mono.from(connectionFactory.create())).map { con ->
        withConnection(con).toFlux()
    }

    val block: Flux<TableRow>? = res.block()
    block?.subscribe { it: TableRow ->
        println(it.toString())
    }
}

private fun withConnection(con: Connection): Flux<TableRow> =
    (con.createStatement(SELECT_SQL).execute()).toFlux().flatMap {
        it.map { r, _ ->
            TableRow(
                r.get("id", Integer::class.java),
                r.get("attr01", String::class.java),
                r.get("attr02", String::class.java)
            )
        }
    }
