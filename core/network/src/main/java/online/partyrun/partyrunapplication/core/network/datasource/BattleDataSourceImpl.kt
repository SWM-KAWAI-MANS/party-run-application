package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.service.BattleApiService
import javax.inject.Inject

class BattleDataSourceImpl @Inject constructor(
    private val battleApi: BattleApiService
) : BattleDataSource {
    override suspend fun getBattleId(): ApiResult<String> =
        battleApi.getBattleId()

}