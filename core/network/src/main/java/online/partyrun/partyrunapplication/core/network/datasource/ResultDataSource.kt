package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse

interface ResultDataSource {
    suspend fun getBattleResults(battleId: String): ApiResult<BattleResultResponse>
}
