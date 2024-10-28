package guilherme.nogueira.network.utils

import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json

class MockVehicles {
    fun getVehiclesMock(): String {
        return try {
            javaClass.classLoader
                ?.getResourceAsStream("vehicles_dataset.json")
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: throw IOException("FILE NOT FOUND")
        } catch (e: IOException) {
            throw IOException(e.message, e)
        }
    }
}