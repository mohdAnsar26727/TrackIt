package track.it.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: Long, // Foreign key to TransactionEntity
    val imageUrl: String // URL or URI of the image
)