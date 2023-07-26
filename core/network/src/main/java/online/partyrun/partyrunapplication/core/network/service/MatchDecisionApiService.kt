package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchDecisionApiService {
    @POST("/api/match")
    suspend fun acceptMatch(
        @Body body: MatchDecisionRequest
    ): ApiResult<MatchStatusResult>

    @POST("/api/match")
    suspend fun declineMatch(
        @Body body: MatchDecisionRequest
    ): ApiResult<MatchStatusResult>
}
