package guilherme.nogueira.bcavehicleapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import guilherme.nogueira.bcavehicleapp.ui.VehicleDetailsScreen
import guilherme.nogueira.bcavehicleapp.ui.VehicleListScreen
import guilherme.nogueira.data.model.VehicleDomainModel
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.VehicleList
    ) {
        composable<Screen.VehicleList> {
            VehicleListScreen(
                onVehicleClick = {
                    navController.navigate(Screen.VehicleDetail(it))
                }
            )
        }

        composable<Screen.VehicleDetail>(
            typeMap = mapOf(
                typeOf<VehicleDomainModel>() to CustomNavType.VehicleNavType,
            )
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.VehicleDetail>()
            VehicleDetailsScreen(
                vehicle = args.vehicle,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}