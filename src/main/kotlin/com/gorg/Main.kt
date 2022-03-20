package com.gorg

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions.*
import io.r2dbc.spi.Result
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.time.Duration

private const val SELECT_SQL = "select * from table01"

fun main() {
    val res = connectionMono().map { con ->
        withConnection(con).toFlux()
    }

    val block: Flux<out Result> = res.block()!!
    block.subscribe { it: Result ->
        tableRowMapper(it).toFlux().subscribe { tr: TableRow -> println(tr) }
    }.toMono().block(Duration.ofSeconds(10))
}

private fun tableRowMapper(r: Result) =
    r.map { row, _ ->
        println("Check ***")
        TableRow(
            row.get("id", Integer::class.java),
            row.get("attr01", String::class.java),
            row.get("attr02", String::class.java)
        )
    }

private fun connectionMono(): Mono<Connection> {
    val connectionFactoryOptions = builder()
        .option(DRIVER, "postgresql")
        .option(HOST, "localhost")
        .option(PORT, 5432)
        .option(USER, "r2dbc")
        .option(PASSWORD, "r2dbc")
        .option(DATABASE, "r2dbc")
        .build()

    return Mono.from(ConnectionFactories.get(connectionFactoryOptions).create())
}

private fun withConnection(con: Connection): Publisher<out Result> = con.createStatement(SELECT_SQL).execute()
