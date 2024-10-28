package guilherme.nogueira.network.datasource

import guilherme.nogueira.network.config.DataResponse
import guilherme.nogueira.network.utils.Mapper

open class DataSource {
    suspend fun <DTO, Domain> apiListCall (
        endpoint: suspend () -> List<DTO>,
        mapper: Mapper<Domain, DTO>,
    ) : DataResponse<List<Domain>> {
        return try {
            val dto = endpoint.invoke()
            val domain = dto.map { mapper.mapToDomainModel(it) }
            DataResponse.Success(domain)
        } catch (e: Throwable){
            DataResponse.Failure(e)
        }
    }
}