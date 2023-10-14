package online.partyrun.partyrunapplication.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.database.model.BattleRunningHistoryEntity

@Dao
interface BattleRunningHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBattleRunningHistory(historyEntities: List<BattleRunningHistoryEntity>)

    @Query("SELECT * FROM battle_running_history_resource")
    fun getAllBattleRunningHistories(): Flow<List<BattleRunningHistoryEntity>>

    @Query("DELETE FROM battle_running_history_resource")
    suspend fun deleteAllBattleRunningHistories()

}
