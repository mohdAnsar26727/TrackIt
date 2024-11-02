package track.it.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import track.it.app.data.local.dao.ImageDao
import track.it.app.data.local.dao.PlanDao
import track.it.app.data.local.dao.TransactionDao
import track.it.app.data.local.entity.ImageEntity
import track.it.app.data.local.entity.PlanEntity
import track.it.app.data.local.entity.TransactionEntity

@Database(
    entities = [PlanEntity::class, TransactionEntity::class, ImageEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun transactionDao(): TransactionDao
    abstract fun imageDao(): ImageDao

    companion object {
        const val DATABASE_NAME = "TrackIT_DB"
    }
}