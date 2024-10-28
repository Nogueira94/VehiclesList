package guilherme.nogueira.network.dto

import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.network.utils.Mapper
import guilherme.nogueira.network.utils.dateFormatter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class VehicleDTO(
    @SerialName("make") val make: String,
    @SerialName("model") val model: String,
    @SerialName("engineSize") val engineSize: String,
    @SerialName("fuel") val fuel: String,
    @SerialName("year") val year: Int,
    @SerialName("mileage") val mileage: Int,
    @SerialName("auctionDateTime") val auctionDateTime: String,
    @SerialName("startingBid") val startingBid: Int,
    @SerialName("favourite") val favourite: Boolean,
    @SerialName("details") val details: VehicleDetailsDTO
)

class VehicleMapper(private val vehicleDetailMapper: VehicleDetailMapper) :
    Mapper<VehicleDomainModel, VehicleDTO> {
    override fun mapToDomainModel(dto: VehicleDTO) = VehicleDomainModel(
        make = dto.make,
        model = dto.model,
        engineSize = dto.engineSize,
        fuel = dto.fuel,
        year = dto.year,
        mileage = dto.mileage,
        auctionDateTime = dto.auctionDateTime,
        startingBid = dto.startingBid,
        isFavourite = dto.favourite,
        details = vehicleDetailMapper.mapToDomainModel(dto.details),
    )
}
