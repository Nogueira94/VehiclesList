package guilherme.nogueira.bcavehicleapp.ui.navigation

import guilherme.nogueira.data.model.VehicleDomainModel
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object VehicleList : Screen()
    @Serializable
    data class VehicleDetail(val vehicle: VehicleDomainModel) : Screen()
}
