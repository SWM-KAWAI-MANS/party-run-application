package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchDecisionApiService {
    @POST("/api/match")
    suspend fun acceptMatch(
        @Body body: MatchDecisionRequest
    ): ApiResult<MatchStatusResponse>

    @POST("/api/match")
    suspend fun declineMatch(
        @Body body: MatchDecisionRequest
    ): ApiResult<MatchStatusResponse>
}
