package io.nicronomicon.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val defaultClient by lazy {
    HttpClient(CIO) {
        install(ContentNegotiation) {
            json(defaultJson)
        }
    }
}

val defaultJson = Json {
    isLenient = true
    ignoreUnknownKeys = true
}