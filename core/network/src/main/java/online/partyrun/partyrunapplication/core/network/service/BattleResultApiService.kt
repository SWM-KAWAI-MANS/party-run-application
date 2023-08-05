package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface BattleResultApiService {

    @POST("/api/battles/{battleId}/finished")
    suspend fun getBattleResults(
        @Path("battleId") battleId: String
    ) : ApiResult<BattleResultResponse>

}
