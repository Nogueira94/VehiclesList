package guilherme.nogueira.domain.usecase

import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.domain.repository.VehicleRepository
import guilherme.nogueira.network.config.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


enum class SortCriteria {
    MAKE, STARTING_BID, MILEAGE, AUCTION_DATE
}

data class VehicleFilters(
    val make: String? = null,
    val model: String? = null,
    val startingBidRange: IntRange? = null,
    val showFavoritesOnly: Boolean = false
)

class GetVehiclesUseCase(
    private val repo: VehicleRepository
) {
    operator fun invoke(
        filters: VehicleFilters? = null,
        sortCriteria: SortCriteria = SortCriteria.AUCTION_DATE
    ): Flow<List<VehicleDomainModel>> = flow {
        when (val result = repo.getVehicles()) {
            is DataResponse.Success -> {
                val vehicles = applySorting(applyFilters(result.data, filters), sortCriteria)
                emit(vehicles)
            }

            is DataResponse.Failure -> throw result.error
        }
    }

    private fun applyFilters(
        vehicles: List<VehicleDomainModel>,
        filters: VehicleFilters?
    ): List<VehicleDomainModel> {
        if (filters == null) return vehicles

        return vehicles.filter { vehicle ->
            var matches = true
            if (filters.make != null) {
                matches = matches && vehicle.make.equals(filters.make, ignoreCase = true)
            }
            if (filters.model != null) {
                matches = matches && vehicle.model.equals(filters.model, ignoreCase = true)
            }
            if (filters.startingBidRange != null) {
                matches = matches && vehicle.startingBid in filters.startingBidRange
            }
            if (filters.showFavoritesOnly) {
                matches = matches && vehicle.isFavourite
            }
            matches
        }
    }

    private fun applySorting(
        vehicles: List<VehicleDomainModel>,
        criteria: SortCriteria
    ): List<VehicleDomainModel> = when (criteria) {
        SortCriteria.MAKE -> vehicles.sortedBy { it.make.lowercase() }
        SortCriteria.STARTING_BID -> vehicles.sortedBy { it.startingBid }
        SortCriteria.MILEAGE -> vehicles.sortedBy { it.mileage }
        SortCriteria.AUCTION_DATE -> vehicles.sortedByDescending { it.auctionDateTime }
    }
}