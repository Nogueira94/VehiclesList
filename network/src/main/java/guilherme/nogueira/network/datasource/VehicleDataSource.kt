package guilherme.nogueira.network.datasource

import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.network.config.DataResponse
import guilherme.nogueira.network.dto.VehicleMapper
import guilherme.nogueira.network.service.VehicleService

class VehicleDataSource(
    private val vehicleService: VehicleService,
    private val vehicleMapper: VehicleMapper,
) : DataSource() {
    suspend fun getVehicles() : DataResponse<List<VehicleDomainModel>> {
        return apiListCall({ vehicleService.getVehicles() }, vehicleMapper)
    }
}