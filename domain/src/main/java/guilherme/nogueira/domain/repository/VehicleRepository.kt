package guilherme.nogueira.domain.repository

import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.network.config.DataResponse
import guilherme.nogueira.network.datasource.VehicleDataSource

interface VehicleRepository {
    suspend fun getVehicles() : DataResponse<List<VehicleDomainModel>>
}

internal class VehicleRepositoryImpl(
    private val vehicleDataSource : VehicleDataSource
) : VehicleRepository {
    override suspend fun getVehicles(): DataResponse<List<VehicleDomainModel>> {
        return vehicleDataSource.getVehicles()
    }
}