package track.it.app.data.mappers

import track.it.app.data.local.entity.TransactionWithImagesEntity
import track.it.app.domain.model.TransactionWithImages

class TransactionWithImagesMapper(
    private val transactionMapper: TransactionMapper,
    private val imageMapper: BillImageMapper
) : ObjectMapper<TransactionWithImages, TransactionWithImagesEntity> {

    override fun toDomain(entity: TransactionWithImagesEntity): TransactionWithImages {
        return TransactionWithImages(
            transactionMapper.toDomain(entity.transaction),
            entity.images.map { imageMapper.toDomain(it) })
    }

    override fun toEntity(domain: TransactionWithImages): TransactionWithImagesEntity {
        return TransactionWithImagesEntity(
            transactionMapper.toEntity(domain.transaction),
            domain.billImages.map { imageMapper.toEntity(it) }
        )
    }
}