package com.ovais.gifster.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIMEOUT = 60000L
private const val BASE_URL = "https://api.giphy.com/v1/"
private const val API_KEY = "RkVNHgj6MiUtOR9xvey4LW1nARq2AWxE"
private const val KEY_API_KEY = "api_key"

object NetworkFactory {
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
            install(HttpTimeout) {
                connectTimeoutMillis = TIMEOUT
                requestTimeoutMillis = TIMEOUT
                socketTimeoutMillis = TIMEOUT
            }
            defaultRequest {
                url(BASE_URL)
                url {
                    parameters.append(KEY_API_KEY, API_KEY)
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}