package guilherme.nogueira.bcavehicleapp.di


import guilherme.nogueira.bcavehicleapp.viewmodel.VehicleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { VehicleViewModel(get()) }
}