package guilherme.nogueira.network.dto

import guilherme.nogueira.data.model.OwnershipDomainModel
import guilherme.nogueira.network.utils.Mapper
import guilherme.nogueira.network.utils.dateFormatter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class OwnershipDTO(
    @SerialName("logBook") val logBook: String,
    @SerialName("numberOfOwners") val numberOfOwners: Int,
    @SerialName("dateOfRegistration") val dateOfRegistration: String
)

class OwnershipMapper : Mapper<OwnershipDomainModel, OwnershipDTO> {
    override fun mapToDomainModel(dto: OwnershipDTO) = OwnershipDomainModel (
        logBook = dto.logBook,
        numberOfOwners = dto.numberOfOwners,
        dateOfRegistration = dto.dateOfRegistration
    )

}
