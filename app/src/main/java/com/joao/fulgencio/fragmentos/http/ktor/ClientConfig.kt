package com.joao.fulgencio.fragmentos.http.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
}