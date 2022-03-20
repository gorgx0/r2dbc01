package com.gorg

import io.r2dbc.proxy.ProxyConnectionFactory
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter
import io.r2dbc.spi.*
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

private const val SELECT_SQL = "select * from table01"

fun close(con: Connection?): Any {
    return con.toMono().subscribe { con?.close() }
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
        .option(DRIVER, "proxy")
        .option(PROTOCOL, "postgresql")
        .option(HOST, "localhost")
        .option(PORT, 5432)
        .option(USER, "r2dbc")
        .option(PASSWORD, "r2dbc")
        .option(DATABASE, "r2dbc")
        .build()

    val connectionFactory = ConnectionFactories.get(connectionFactoryOptions)

    val executionInfoFormatter = QueryExecutionInfoFormatter.showAll()

    val proxyConnectionFactory: ConnectionFactory = ProxyConnectionFactory.builder(connectionFactory)
        .onAfterQuery { execInfo ->
            println(executionInfoFormatter.format(execInfo))
        }
        .build()

    return proxyConnectionFactory.create()
}