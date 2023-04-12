package io.nicronomicon.plugins

import graphql.GraphQL
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.asCompletableFuture
import java.util.concurrent.CompletableFuture


fun Application.configureGraphQl() {

    routing {
        get("graphql") {
            val schema = "type Query{hello: String}"

            val schemaParser = SchemaParser()
            val typeDefinitionRegistry = schemaParser.parse(schema)

            val runtimeWiring = newRuntimeWiring()
                .type("Query") { builder ->
                    builder.dataFetcher(
                        "hello",
                        CoroutineFetcher {
                            delay(100)
                            "Hello Async World"
                        }
                    )
                }
                .build()

            val schemaGenerator = SchemaGenerator()
            val graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)

            val build = GraphQL.newGraphQL(graphQLSchema).build()
            val executionResult = build.execute("{hello}")

            call.respond(executionResult.getData())
        }

    }
}

@OptIn(DelicateCoroutinesApi::class)
class CoroutineFetcher<T>(private val fetcher: suspend () -> T) : DataFetcher<CompletableFuture<T>> {
    override fun get(environment: DataFetchingEnvironment?): CompletableFuture<T> {
        return GlobalScope.async() {
            fetcher()
        }.asCompletableFuture()
    }

}
