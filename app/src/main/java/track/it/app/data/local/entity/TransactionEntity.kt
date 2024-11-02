package track.it.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long, // Foreign key to the PlanEntity
    val to: String,
    val note: String,
    val amount: Double,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class TransactionWithImagesEntity(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "transactionId"
    )
    val images: List<ImageEntity>
)
