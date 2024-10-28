package guilherme.nogueira.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OwnershipDomainModel(
    val logBook: String,
    val numberOfOwners: Int,
    val dateOfRegistration: String
) : Parcelable