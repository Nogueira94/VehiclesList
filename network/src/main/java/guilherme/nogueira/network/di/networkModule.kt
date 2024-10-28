package guilherme.nogueira.network.di

import guilherme.nogueira.network.client.KtorClient
import guilherme.nogueira.network.datasource.VehicleDataSource
import guilherme.nogueira.network.dto.OwnershipMapper
import guilherme.nogueira.network.dto.SpecificationMapper
import guilherme.nogueira.network.dto.VehicleDetailMapper
import guilherme.nogueira.network.dto.VehicleMapper
import guilherme.nogueira.network.service.VehicleService
import guilherme.nogueira.network.service.VehicleServiceImpl
import guilherme.nogueira.network.utils.MockVehicles
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {

    single { VehicleMapper(vehicleDetailMapper = get()) }
    single { VehicleDetailMapper(specificationMapper = get(), ownershipMapper = get()) }
    single { OwnershipMapper() }
    single { SpecificationMapper() }

    single { MockVehicles() }

    single<VehicleDataSource>{
        VehicleDataSource(
            vehicleService = get(),
            vehicleMapper = get()
        )
    }

    single { KtorClient(get()) }

    single<HttpClient> { get<KtorClient>().client() }

    single<VehicleService> {
        VehicleServiceImpl(get())
    }


}