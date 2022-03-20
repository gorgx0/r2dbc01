package com.gorg

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions.*
import io.r2dbc.spi.Result
import io.r2dbc.spi.Row
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

private const val SELECT_SQL = "select * from table01"

fun close(con: Connection?): Any {
    return con.toMono().subscribe { close(con) }
}

fun main() {

    val monoResult: Mono<Result> = connectionMono().toMono().flatMap {
        con ->
           Mono.from(
               con.createStatement(SELECT_SQL).execute())
               .doFinally {
                   close(con)
               }
    }

    val disposable: Mono<Publisher<TableRow>> = monoResult.map { result ->
        result.map { r, _ ->
            tableRowMapper(r)
        }
    }

//    val flatMap: Mono<Flux<TableRow>> = result.map { it: Result -> Flux.from(it.map { r, _ -> tableRowMapper(r) }) }
    println(":::START:::")
    val block: Publisher<TableRow>? = disposable.block()
    block?.toFlux()?.subscribe {
        println(it)
    }

//    val rowFlux: Flux<TableRow> = flatMap.block()
//    rowFlux.subscribe {
//        println(">>>> ${}")
//    }
//    println("bl_1" + rowFlux)

}

private fun tableRowMapper(r: Row): TableRow {
    println("Check ***")
    return TableRow(
        r.get("id", Integer::class.java),
        r.get("attr01", String::class.java),
        r.get("attr02", String::class.java)
    )
}

private fun connectionMono(): Publisher<out Connection> {
    val connectionFactoryOptions = builder()
        .option(DRIVER, "postgresql")
        .option(HOST, "localhost")
        .option(PORT, 5432)
        .option(USER, "r2dbc")
        .option(PASSWORD, "r2dbc")
        .option(DATABASE, "r2dbc")
        .build()

    return ConnectionFactories.get(connectionFactoryOptions).create()
}