package track.it.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import track.it.app.data.local.entity.ImageEntity

@Dao
interface ImageDao {

    // Insert a new image
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageEntity): Long

    // Insert multiple images
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>): LongArray

    // Delete an image
    @Delete
    suspend fun deleteImage(image: ImageEntity): Int

    @Delete
    suspend fun deleteImage(image: List<ImageEntity>): Int

    // Get all images for a specific transaction
    @Query("SELECT * FROM images WHERE transactionId = :transactionId")
    suspend fun getImagesForTransaction(transactionId: Long): List<ImageEntity>
}