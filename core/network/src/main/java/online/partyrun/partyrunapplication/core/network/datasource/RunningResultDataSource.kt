package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse

interface RunningResultDataSource {
    suspend fun getBattleResults(battleId: String): ApiResult<BattleResultResponse>
}
