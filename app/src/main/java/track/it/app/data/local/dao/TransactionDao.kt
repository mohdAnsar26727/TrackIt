package track.it.app.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import track.it.app.data.local.entity.TransactionEntity
import track.it.app.data.local.entity.TransactionWithImagesEntity

@Dao
interface TransactionDao {

    // Insert a new transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    // Update an existing transaction
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int

    // Delete a transaction
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity): Int

    @Transaction
    @Query("SELECT * FROM transactions WHERE id=:id LIMIT 1")
    fun getTransactionById(id: Long): TransactionWithImagesEntity?

    // Fetch all transactions for a specific plan with pagination
    @Query("SELECT * FROM transactions WHERE planId = :planId ORDER BY createdAt DESC")
    fun getTransactionsByPlanIdPaged(planId: Int): PagingSource<Int, TransactionEntity>

    @Query("SELECT * FROM transactions WHERE planId = :planId")
    suspend fun getTransactionsByPlanId(planId: Long): List<TransactionEntity>

    @Transaction
    @Query("SELECT * FROM transactions WHERE planId = :planId ORDER BY createdAt DESC")
    fun getTransactionsWithImagesByPlanIdPaged(planId: Long): PagingSource<Int, TransactionWithImagesEntity>

    @Query("DELETE FROM transactions WHERE planId = :planId")
    fun deleteTransactionsByPlanId(planId: Long): Int
}
