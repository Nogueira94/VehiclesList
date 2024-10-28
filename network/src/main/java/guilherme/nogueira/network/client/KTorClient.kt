package guilherme.nogueira.network.client

import guilherme.nogueira.network.utils.MockVehicles
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


const val API_URL = "https://www.api.example.com/"

class KtorClient(
    private val mockVehicles: MockVehicles
) {
    private val mockVehiclesAPI = MockEngine { request ->
        when (request.url.encodedPath) {
            "/vehicles" -> {
                respond(
                    content = mockVehicles.getVehiclesMock(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType, ContentType.Application.Json.toString()
                    )
                )
            }
            else -> error("PATH NOT FOUND")
        }
    }

    private val client = HttpClient(mockVehiclesAPI) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }

        defaultRequest {
            url(API_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    fun client(): HttpClient = client
}