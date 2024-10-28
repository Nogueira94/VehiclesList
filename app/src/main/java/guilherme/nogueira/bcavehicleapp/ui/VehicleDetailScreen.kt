package guilherme.nogueira.bcavehicleapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.domain.utils.formatForDisplay
import guilherme.nogueira.domain.utils.toLocalDateTime

@Composable
fun VehicleDetailsScreen(
    vehicle: VehicleDomainModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${vehicle.make} ${vehicle.model}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Section(title = "Specification") {
                with(vehicle.details.specification) {
                    DetailRow("Type", vehicleType)
                    DetailRow("Color", colour)
                    DetailRow("Transmission", transmission)
                    DetailRow("Doors", numberOfDoors.toString())
                    DetailRow("CO2 Emissions", co2Emissions)
                    DetailRow("NOX Emissions", noxEmissions.toString())
                    DetailRow("Keys", numberOfKeys.toString())
                }
            }

            // Ownership Section
            Section(title = "Ownership") {
                with(vehicle.details.ownership) {
                    DetailRow("Logbook", logBook)
                    DetailRow("Owners", numberOfOwners.toString())
                    DetailRow("Registration Date", dateOfRegistration.toLocalDateTime().formatForDisplay())
                }
            }

            // Equipment Section
            Section(title = "Equipment") {
                vehicle.details.equipment.forEach { item ->
                    Text("• $item")
                }
            }
        }
    }

}

@Composable
private fun Section(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value)
    }
}
