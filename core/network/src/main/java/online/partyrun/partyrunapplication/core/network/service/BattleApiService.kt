package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleIdResponse
import online.partyrun.partyrunapplication.core.network.model.response.TerminateBattleResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface BattleApiService {
    @GET("/api/battles/join")
    suspend fun getBattleId(): ApiResult<BattleIdResponse>

    @POST("/api/battles/runners/finished")
    suspend fun terminateOngoingBattle(): ApiResult<TerminateBattleResponse>
}
