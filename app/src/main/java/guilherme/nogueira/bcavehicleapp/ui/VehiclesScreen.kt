package guilherme.nogueira.bcavehicleapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import guilherme.nogueira.bcavehicleapp.R
import guilherme.nogueira.bcavehicleapp.viewmodel.VehicleEvent
import guilherme.nogueira.bcavehicleapp.viewmodel.VehicleViewModel
import guilherme.nogueira.data.model.VehicleDomainModel
import guilherme.nogueira.domain.usecase.SortCriteria
import guilherme.nogueira.domain.utils.timeUntil
import guilherme.nogueira.domain.utils.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import kotlin.math.ceil

@Composable
fun VehicleListScreen(
    onVehicleClick: (VehicleDomainModel) -> Unit,
    viewModel: VehicleViewModel = koinViewModel<VehicleViewModel>()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            FilterSection(
                onFilterChange = { make, model, bidRange, favoritesOnly ->
                    viewModel.dispatch(
                        VehicleEvent.FilterVehicles(
                            make = make,
                            model = model,
                            startingBidRange = bidRange,
                            showFavoritesOnly = favoritesOnly
                        )
                    )
                },
                onClearFilter = { viewModel.dispatch(VehicleEvent.ClearFilter) }
            )
            SortControls(
                currentSort = state.sortCriteria,
                onSortChange = { sortCriteria : SortCriteria ->
                    viewModel.dispatch(VehicleEvent.SortVehicles(sortCriteria))
                }
            )

            if(state.isLoading){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(200.dp)
                    )
                }

            }

            if(state.vehicles.isNotEmpty()){
                LazyColumn {
                    val pageStart = (state.currentPage - 1) * state.pageSize
                    val pageEnd = minOf(pageStart + state.pageSize, state.vehicles.size)
                    val pageItems = state.vehicles.subList(pageStart, pageEnd)

                    items(pageItems) { vehicle ->
                        VehicleCard(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            vehicle = vehicle,
                            onClick = {
                                onVehicleClick.invoke(it)
                            }
                        )
                    }
                    item {
                        PaginationControls(
                            currentPage = state.currentPage,
                            pageSize = state.pageSize,
                            totalItems = state.vehicles.size,
                            onPageChange = { page ->
                                viewModel.dispatch(VehicleEvent.NavigateToPage(page))
                            },
                            onPageSizeChange = { size ->
                                viewModel.dispatch(VehicleEvent.UpdatePageSize(size))
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun VehicleCard(
    vehicle: VehicleDomainModel,
    onClick: (VehicleDomainModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(vehicle) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${vehicle.make} ${vehicle.model}",
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = if (vehicle.isFavourite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.bca_placeholder),
                        contentDescription = "BCA PLACEHOLDER",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Engine: ${vehicle.engineSize} ${vehicle.fuel}")
                    Text("Year: ${vehicle.year}")
                    Text("Mileage: ${vehicle.mileage} miles")
                    Text("Starting Bid: £${vehicle.startingBid}")
                    Text(
                        text = "Auction: ${vehicle.auctionDateTime.toLocalDateTime().timeUntil()}",
                        color = if (vehicle.auctionDateTime.toLocalDateTime().isBefore(LocalDateTime.now())) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleCard2(
    vehicle: VehicleDomainModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${vehicle.make} ${vehicle.model}",
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = if (vehicle.isFavourite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.bca_placeholder),
                    contentDescription = "BCA PLACEHOLDER"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Engine: ${vehicle.engineSize} ${vehicle.fuel}")
            Text("Year: ${vehicle.year}")
            Text("Mileage: ${vehicle.mileage} miles")
            Text("Starting Bid: £${vehicle.startingBid}")
            Text(
                text = "Auction: ${vehicle.auctionDateTime.toLocalDateTime().timeUntil()}",
                color = if (vehicle.auctionDateTime.toLocalDateTime().isBefore(LocalDateTime.now())) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun FilterSection(
    onFilterChange: (String?, String?, IntRange?, Boolean) -> Unit,
    onClearFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    var make by remember { mutableStateOf<String?>(null) }
    var model by remember { mutableStateOf<String?>(null) }
    var minBid by remember { mutableStateOf<String?>(null) }
    var maxBid by remember { mutableStateOf<String?>(null) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val hasActiveFilters = remember(make, model, minBid, maxBid, showFavoritesOnly) {
        make != null || model != null || minBid != null ||
                maxBid != null || showFavoritesOnly
    }

    Column(modifier = modifier.padding(16.dp)) {
        AnimatedVisibility(
            visible = hasActiveFilters,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                androidx.compose.material3.FilledTonalButton(
                    onClick = {
                        make = null
                        model = null
                        minBid = null
                        maxBid = null
                        showFavoritesOnly = false
                        onClearFilter()
                    }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear Filters",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clear Filters")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = make ?: String(),
                singleLine = true,
                maxLines = 1,
                onValueChange = {
                    make = it.takeIf { it.isNotEmpty() }
                    onFilterChange(make, model, getBidRange(minBid, maxBid), showFavoritesOnly)
                },
                placeholder = { Text("Make") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = model ?: String(),
                onValueChange = {
                    model = it.takeIf { it.isNotEmpty() }
                    onFilterChange(make, model, getBidRange(minBid, maxBid), showFavoritesOnly)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                maxLines = 1,
                placeholder = { Text("Model")},
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = minBid ?: String(),
                onValueChange = {
                    minBid = it
                    onFilterChange(make, model, getBidRange(minBid, maxBid), showFavoritesOnly)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number ,imeAction = ImeAction.Done),
                singleLine = true,
                maxLines = 1,
                placeholder = { Text("Min Bid") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = maxBid ?: String(),
                onValueChange = {
                    maxBid = it
                    onFilterChange(make, model, getBidRange(minBid, maxBid), showFavoritesOnly)
                },
                placeholder = { Text("Max Bid") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number ,imeAction = ImeAction.Done),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showFavoritesOnly,
                onCheckedChange = {
                    showFavoritesOnly = it
                    onFilterChange(make, model, getBidRange(minBid, maxBid), showFavoritesOnly)
                }
            )
            Text("Show Favorites Only")
        }
    }
}

private fun getBidRange(minBid: String?, maxBid: String?): IntRange? {
    val min = minBid?.toInt() ?: Int.MIN_VALUE
    val max = maxBid?.toInt() ?: Int.MAX_VALUE
    return if (min <= max) {
        min..max
    } else null
}



@Composable
fun SortControls(
    currentSort: SortCriteria,
    onSortChange: (SortCriteria) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortCriteria.entries.forEach { criteria ->
            FilterChip(
                selected = currentSort == criteria,
                onClick = { onSortChange(criteria) },
                label = { Text(criteria.name.replace('_', ' ')) }
            )
        }
    }
}

@Composable
fun PaginationControls(
    currentPage: Int,
    pageSize: Int,
    totalItems: Int,
    onPageChange: (Int) -> Unit,
    onPageSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPages = ceil(totalItems.toFloat() / pageSize).toInt()
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onPageChange(currentPage - 1) },
                enabled = currentPage > 1
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous Page")
            }

            Text("Page $currentPage of $totalPages")

            IconButton(
                onClick = { onPageChange(currentPage + 1) },
                enabled = currentPage < totalPages
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next Page")
            }
        }

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Items per page: $pageSize")
                IconButton(onClick = { isDropdownExpanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, "Change page size")
                }
            }
            DropdownMenu(
                modifier = Modifier.align(Alignment.BottomEnd),
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
            ) {
                listOf(10, 20, 50).forEach { size ->
                    DropdownMenuItem(
                        text = { Text("$size") },
                        onClick = {
                            onPageSizeChange(size)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}
