package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleIdResponse
import online.partyrun.partyrunapplication.core.network.model.response.TerminateBattleResponse
import online.partyrun.partyrunapplication.core.network.service.BattleApiService
import javax.inject.Inject

class BattleDataSourceImpl @Inject constructor(
    private val battleApi: BattleApiService
) : BattleDataSource {
    override suspend fun getBattleId(): ApiResponse<BattleIdResponse> =
        battleApi.getBattleId()

    override suspend fun terminateOngoingBattle(): ApiResponse<TerminateBattleResponse> =
        battleApi.terminateOngoingBattle()
}
