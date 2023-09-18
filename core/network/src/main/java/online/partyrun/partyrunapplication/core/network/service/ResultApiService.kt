package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import online.partyrun.partyrunapplication.core.network.model.response.SingleResultResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ResultApiService {

    @GET("/api/battles/{battleId}")
    suspend fun getBattleResults(
        @Path("battleId") battleId: String
    ): ApiResponse<BattleResultResponse>

    @GET("/api/singles/{singleId}")
    suspend fun getSingleResults(
        @Path("singleId") singleId: String
    ): ApiResponse<SingleResultResponse>

}
