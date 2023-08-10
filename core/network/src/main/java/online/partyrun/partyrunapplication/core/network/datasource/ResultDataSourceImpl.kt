package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import online.partyrun.partyrunapplication.core.network.service.ResultApiService
import javax.inject.Inject

class ResultDataSourceImpl @Inject constructor(
    private val resultApi: ResultApiService
) : ResultDataSource {
    override suspend fun getBattleResults(battleId: String): ApiResult<BattleResultResponse> =
        resultApi.getBattleResults(battleId)

}
