package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleIdResponse
import retrofit2.http.GET

interface BattleApiService {
    @GET("/api/battles/join")
    suspend fun getBattleId(): ApiResult<BattleIdResponse>
}
