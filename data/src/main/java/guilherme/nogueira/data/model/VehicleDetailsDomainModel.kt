package guilherme.nogueira.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class VehicleDetailsDomainModel(
    val specification: SpecificationDomainModel,
    val ownership: OwnershipDomainModel,
    val equipment: List<String>
) : Parcelable
