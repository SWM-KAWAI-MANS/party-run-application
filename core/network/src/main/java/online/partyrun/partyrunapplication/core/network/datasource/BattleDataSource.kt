package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleIdResponse

interface BattleDataSource {
    suspend fun getBattleId(): ApiResult<BattleIdResponse>
}
