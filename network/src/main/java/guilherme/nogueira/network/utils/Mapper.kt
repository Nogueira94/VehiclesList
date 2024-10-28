package guilherme.nogueira.network.utils

interface Mapper<DomainModel,DTO> {
    fun mapToDomainModel(dto: DTO) : DomainModel
}