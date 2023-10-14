package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.running_result.battle.BattleResult
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.my_page.BattleRunningHistory
import online.partyrun.partyrunapplication.core.model.my_page.ComprehensiveRunRecord
import online.partyrun.partyrunapplication.core.model.my_page.SingleRunningHistory
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult

interface ResultRepository {
    suspend fun getBattleResults(): Flow<Result<BattleResult>>
    suspend fun getSingleResults(): Flow<Result<SingleResult>>
    suspend fun getComprehensiveRunRecord(): Flow<Result<ComprehensiveRunRecord>>
    suspend fun updateSingleHistory(): Flow<Result<Unit>>
    suspend fun updateBattleHistory(): Flow<Result<Unit>>
    suspend fun getSingleHistory(): Flow<Result<SingleRunningHistory>>
    suspend fun getBattleHistory(): Flow<Result<BattleRunningHistory>>

}
