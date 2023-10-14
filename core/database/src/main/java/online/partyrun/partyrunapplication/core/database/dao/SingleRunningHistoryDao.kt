package online.partyrun.partyrunapplication.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.database.model.SingleRunningHistoryEntity

@Dao
interface SingleRunningHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSingleRunningHistory(historyEntities: List<SingleRunningHistoryEntity>)

    @Query("SELECT * FROM single_running_history_resource")
    fun getAllSingleRunningHistories(): Flow<List<SingleRunningHistoryEntity>>

}
