package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.running_result.battle.BattleResult
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.my_page.BattleRunningHistory
import online.partyrun.partyrunapplication.core.model.my_page.ComprehensiveRunRecord
import online.partyrun.partyrunapplication.core.model.my_page.SingleRunningHistory
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult

interface ResultRepository {
    suspend fun getBattleResults(): Result<BattleResult>
    suspend fun getSingleResults(): Result<SingleResult>
    suspend fun getComprehensiveRunRecord(): Result<ComprehensiveRunRecord>
    fun updateSingleHistory(): Flow<Result<Unit>>
    fun updateBattleHistory(): Flow<Result<Unit>>
    fun getSingleHistory(): Flow<Result<SingleRunningHistory>>
    fun getBattleHistory(): Flow<Result<BattleRunningHistory>>
    fun deleteAllHistories(): Flow<Result<Unit>>

}
