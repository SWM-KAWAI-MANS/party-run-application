package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchDecisionApiService {
    @POST("/api/matching/members/join")
    suspend fun acceptMatch(
        @Body body: MatchDecisionRequest
    ): ApiResponse<MatchStatusResponse>

    @POST("/api/matching/members/join")
    suspend fun declineMatch(
        @Body body: MatchDecisionRequest
    ): ApiResponse<MatchStatusResponse>
}
