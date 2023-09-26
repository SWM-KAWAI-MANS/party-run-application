package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleIdResponse
import online.partyrun.partyrunapplication.core.network.model.response.TerminateBattleResponse

interface BattleDataSource {
    suspend fun getBattleId(): ApiResponse<BattleIdResponse>
    suspend fun terminateOngoingBattle(): ApiResponse<TerminateBattleResponse>
}
