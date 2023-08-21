package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.common.result.Result

interface ResultRepository {
    suspend fun getBattleResults(): Flow<Result<BattleResult>>
}
