package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult

interface ResultRepository {
    suspend fun getBattleResults(): Flow<ApiResponse<BattleResult>>
}
