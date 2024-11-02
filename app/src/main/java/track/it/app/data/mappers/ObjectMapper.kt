package track.it.app.data.mappers

interface ObjectMapper<D, E> {
    fun toDomain(entity: E): D
    fun toEntity(domain: D): E
}