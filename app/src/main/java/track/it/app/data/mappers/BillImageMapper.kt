package track.it.app.data.mappers

import track.it.app.data.local.entity.ImageEntity
import track.it.app.domain.model.BillImage

class BillImageMapper : ObjectMapper<BillImage, ImageEntity> {
    override fun toDomain(entity: ImageEntity): BillImage {
        return BillImage(
            id = entity.id,
            planId = entity.planId,
            transactionId = entity.transactionId,
            imageUrl = entity.imageUrl
        )
    }

    override fun toEntity(domain: BillImage): ImageEntity {
        return ImageEntity(
            id = domain.id,
            transactionId = domain.transactionId,
            planId = domain.planId,
            imageUrl = domain.imageUrl
        )
    }
}