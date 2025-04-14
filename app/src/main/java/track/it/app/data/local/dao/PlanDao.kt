package track.it.app.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import track.it.app.data.local.entity.PlanEntity

@Dao
interface PlanDao {

    // Insert a new plan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlan(plan: PlanEntity): Long

    // Update an existing plan
    @Update
    suspend fun updatePlan(plan: PlanEntity): Int

    // Delete a plan
    @Delete
    suspend fun deletePlan(plan: PlanEntity): Int

    @Query("DELETE FROM plans WHERE id = :planId")
    suspend fun deletePlan(planId: Long): Int

    // Fetch all plans with pagination support
    @RawQuery(observedEntities = [PlanEntity::class])
    fun getAllPlansPaged(query: SupportSQLiteQuery): PagingSource<Int, PlanEntity>

    // Fetch a specific plan by ID
    @Query("SELECT * FROM plans WHERE id = :planId")
    suspend fun getPlanById(planId: Long): PlanEntity?
}