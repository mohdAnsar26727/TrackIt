package track.it.app.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import track.it.app.data.local.entity.PlanEntity

@Dao
interface PlanDao {

    // Insert a new plan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: PlanEntity): Long

    // Update an existing plan
    @Update
    suspend fun updatePlan(plan: PlanEntity): Int

    // Delete a plan
    @Delete
    suspend fun deletePlan(plan: PlanEntity): Int

    // Fetch all plans with pagination support
    @Query("SELECT * FROM plans ORDER BY createdAt DESC")
    fun getAllPlansPaged(): PagingSource<Int, PlanEntity>

    // Fetch a specific plan by ID
    @Query("SELECT * FROM plans WHERE id = :planId")
    suspend fun getPlanById(planId: Long): PlanEntity?

    @Query("SELECT * FROM plans ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPlansPaginated(
        offset: Int,
        limit: Int
    ): List<PlanEntity>
}