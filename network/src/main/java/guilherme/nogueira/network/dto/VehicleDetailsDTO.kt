package guilherme.nogueira.network.dto

import guilherme.nogueira.data.model.VehicleDetailsDomainModel
import guilherme.nogueira.network.utils.Mapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDetailsDTO(
    @SerialName("specification") val specification: SpecificationDTO,
    @SerialName("ownership") val ownership: OwnershipDTO,
    @SerialName("equipment") val equipment: List<String>
)

class VehicleDetailMapper(
    private val specificationMapper: SpecificationMapper,
    private val ownershipMapper: OwnershipMapper
) : Mapper<VehicleDetailsDomainModel, VehicleDetailsDTO> {
    override fun mapToDomainModel(dto: VehicleDetailsDTO) = VehicleDetailsDomainModel(
        specification = specificationMapper.mapToDomainModel(dto.specification),
        ownership = ownershipMapper.mapToDomainModel(dto.ownership),
        equipment = dto.equipment
    )

}