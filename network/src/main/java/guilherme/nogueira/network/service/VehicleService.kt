package guilherme.nogueira.network.service

import guilherme.nogueira.network.dto.VehicleDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface VehicleService {
    suspend fun getVehicles() : List<VehicleDTO>
}

class VehicleServiceImpl(
    private val client: HttpClient
) : VehicleService {
    override suspend fun getVehicles(): List<VehicleDTO> {
        return client.get("vehicles").body()
    }
}