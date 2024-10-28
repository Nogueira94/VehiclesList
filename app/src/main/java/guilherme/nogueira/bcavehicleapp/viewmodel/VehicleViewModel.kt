package guilherme.nogueira.bcavehicleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.domain.usecase.GetVehiclesUseCase
import guilherme.nogueira.domain.usecase.SortCriteria
import guilherme.nogueira.domain.usecase.VehicleFilters
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDateTime

sealed class VehicleEvent {
    data class FilterVehicles(
        val make: String? = null,
        val model: String? = null,
        val startingBidRange: IntRange? = null,
        val showFavoritesOnly: Boolean = false
    ) : VehicleEvent()
    data class SortVehicles(val sortBy: SortCriteria) : VehicleEvent()
    data class UpdatePageSize(val size: Int) : VehicleEvent()
    data class NavigateToPage(val page: Int) : VehicleEvent()
    data object ClearFilter : VehicleEvent()
}

data class VehicleViewState(
    val vehicles: List<VehicleDomainModel> = emptyList(),
    val sortCriteria: SortCriteria = SortCriteria.AUCTION_DATE,
    val selectedVehicle: VehicleDomainModel? = null,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val filters: VehicleFilters = VehicleFilters()
)

class VehicleViewModel(
    private val useCase: GetVehiclesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VehicleViewState())
    val state: StateFlow<VehicleViewState> = _state


    private var job: Job? = null

    init {
        loadVehicles()
    }

    private fun loadVehicles(
        filters: VehicleFilters? = null,
        sortCriteria: SortCriteria = SortCriteria.AUCTION_DATE
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            useCase.invoke(filters, sortCriteria)
                .catch {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = true
                    )
                }
                .collect { vehicles ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        vehicles = vehicles,
                    )
                }
        }
    }

    fun dispatch(intent: VehicleEvent) {
        when (intent) {
            is VehicleEvent.FilterVehicles -> {
                val newFilters = VehicleFilters(
                    make = intent.make,
                    model = intent.model,
                    startingBidRange = intent.startingBidRange,
                    showFavoritesOnly = intent.showFavoritesOnly
                )
                _state.value = _state.value.copy(filters = newFilters)
                loadVehicles(newFilters, _state.value.sortCriteria)
            }
            is VehicleEvent.SortVehicles -> {
                _state.value = _state.value.copy(sortCriteria = intent.sortBy)
                loadVehicles(_state.value.filters, intent.sortBy)
            }
            is VehicleEvent.UpdatePageSize -> updatePageSize(intent.size)
            is VehicleEvent.NavigateToPage -> navigateToPage(intent.page)
            is VehicleEvent.ClearFilter -> {
                loadVehicles(null)
            }
        }
    }

    private fun updatePageSize(size: Int) {
        _state.value = _state.value.copy(
            pageSize = size,
            currentPage = 1
        )
    }

    private fun navigateToPage(page: Int) {
        _state.value = _state.value.copy(currentPage = page)
    }

    fun getTimeUntilAuction(auctionDateTime: LocalDateTime): String {
        return try {
            val now = LocalDateTime.now()
            val duration = java.time.Duration.between(now, auctionDateTime)

            val days = duration.toDays()
            val hours = duration.toHours() % 24

            "${days}d ${hours}h"
        } catch (e: Exception) {
            "Invalid date"
        }
    }


    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

}