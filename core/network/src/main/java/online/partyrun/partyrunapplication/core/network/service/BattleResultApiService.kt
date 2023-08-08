package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BattleResultApiService {

    @GET("/api/battles/{battleId}")
    suspend fun getBattleResults(
        @Path("battleId") battleId: String
    ) : ApiResult<BattleResultResponse>

}
