package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import online.partyrun.partyrunapplication.core.network.service.BattleResultApiService
import javax.inject.Inject

class RunningResultDataSourceImpl @Inject constructor(
    private val battleResultApi: BattleResultApiService
) : RunningResultDataSource {
    override suspend fun getBattleResults(battleId: String): ApiResult<BattleResultResponse> =
        battleResultApi.getBattleResults(battleId)

}
