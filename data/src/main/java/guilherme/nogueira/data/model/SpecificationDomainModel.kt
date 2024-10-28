package guilherme.nogueira.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SpecificationDomainModel (
    val vehicleType: String,
    val colour: String,
    val fuel: String,
    val transmission: String,
    val numberOfDoors: Int,
    val co2Emissions: String,
    val noxEmissions: Int,
    val numberOfKeys: Int
) : Parcelable
