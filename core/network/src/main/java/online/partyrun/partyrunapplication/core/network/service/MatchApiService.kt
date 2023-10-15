package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.CancelMatchResponse
import online.partyrun.partyrunapplication.core.network.model.response.MatchInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MatchApiService {
    @POST("/api/waiting")
    suspend fun registerMatch(
        @Body body: RunningDistanceRequest
    ): ApiResponse<MatchStatusResponse>

    @POST("/api/matching/members/join")
    suspend fun acceptMatch(
        @Body body: MatchDecisionRequest
    ): ApiResponse<MatchStatusResponse>

    @POST("/api/matching/members/join")
    suspend fun declineMatch(
        @Body body: MatchDecisionRequest
    ): ApiResponse<MatchStatusResponse>

    @GET("/api/matching/recent/members")
    suspend fun getRunnerIds(): ApiResponse<MatchInfoResponse>

    @POST("/api/waiting/event/cancel")
    suspend fun cancelMatchWaitingEvent(): ApiResponse<CancelMatchResponse>

}
