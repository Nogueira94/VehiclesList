package guilherme.nogueira.network.dto

import guilherme.nogueira.data.model.SpecificationDomainModel
import guilherme.nogueira.network.utils.Mapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpecificationDTO(
    @SerialName("vehicleType") val vehicleType: String,
    @SerialName("colour") val colour: String,
    @SerialName("fuel") val fuel: String,
    @SerialName("transmission") val transmission: String,
    @SerialName("numberOfDoors") val numberOfDoors: Int,
    @SerialName("co2Emissions") val co2Emissions: String,
    @SerialName("noxEmissions") val noxEmissions: Int,
    @SerialName("numberOfKeys") val numberOfKeys: Int
)

class SpecificationMapper : Mapper<SpecificationDomainModel, SpecificationDTO> {
    override fun mapToDomainModel(dto: SpecificationDTO) = SpecificationDomainModel(
        vehicleType = dto.vehicleType,
        colour = dto.colour,
        fuel = dto.fuel,
        transmission = dto.transmission,
        numberOfDoors = dto.numberOfDoors,
        co2Emissions = dto.co2Emissions,
        noxEmissions = dto.noxEmissions,
        numberOfKeys = dto.numberOfKeys,
    )
}
