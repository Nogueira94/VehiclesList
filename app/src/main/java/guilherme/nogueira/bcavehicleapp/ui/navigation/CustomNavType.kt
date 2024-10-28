package guilherme.nogueira.bcavehicleapp.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import guilherme.nogueira.data.model.VehicleDomainModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val VehicleNavType = object : NavType<VehicleDomainModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): VehicleDomainModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): VehicleDomainModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: VehicleDomainModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: VehicleDomainModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

}