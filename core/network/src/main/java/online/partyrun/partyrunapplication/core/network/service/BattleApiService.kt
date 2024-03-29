package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleIdResponse
import online.partyrun.partyrunapplication.core.network.model.response.TerminateBattleResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface BattleApiService {
    @GET("/api/battles/join")
    suspend fun getBattleId(): ApiResponse<BattleIdResponse>

    @POST("/api/battles/runners/finished")
    suspend fun terminateOngoingBattle(): ApiResponse<TerminateBattleResponse>
}
