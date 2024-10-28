package guilherme.nogueira.domain.di

import guilherme.nogueira.domain.repository.VehicleRepository
import guilherme.nogueira.domain.repository.VehicleRepositoryImpl
import guilherme.nogueira.domain.usecase.GetVehiclesUseCase
import org.koin.dsl.module

val domainModule = module {
    single<VehicleRepository> {
        VehicleRepositoryImpl(
            vehicleDataSource = get()
        )
    }
    single {
        GetVehiclesUseCase(repo = get())
    }
}