package guilherme.nogueira.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class VehicleDomainModel(
    val make: String,
    val model: String,
    val engineSize: String,
    val fuel: String,
    val year: Int,
    val mileage: Int,
    val auctionDateTime: String,
    val startingBid: Int,
    val isFavourite: Boolean,
    val details: VehicleDetailsDomainModel
) : Parcelable
