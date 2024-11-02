package track.it.app.data.mappers

import track.it.app.data.local.entity.TransactionEntity
import track.it.app.domain.model.Transaction
import track.it.app.domain.model.TransactionStatus


class TransactionMapper : ObjectMapper<Transaction, TransactionEntity> {
    override fun toDomain(entity: TransactionEntity): Transaction {
        return Transaction(
            id = entity.id,
            planId = entity.planId,
            to = entity.to,
            note = entity.note,
            amount = entity.amount,
            status = TransactionStatus.valueOf(entity.status),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(domain: Transaction): TransactionEntity {
        return TransactionEntity(
            id = domain.id,
            planId = domain.planId,
            to = domain.to,
            note = domain.note,
            amount = domain.amount,
            status = domain.status.name,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
}
