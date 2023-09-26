package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.CancelMatchResponse
import online.partyrun.partyrunapplication.core.network.model.response.MatchInfoResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface MatchApiService {

    @GET("/api/matching/recent/members")
    suspend fun getRunnerIds(): ApiResponse<MatchInfoResponse>

    @POST("/api/waiting/event/cancel")
    suspend fun cancelMatchWaitingEvent(): ApiResponse<CancelMatchResponse>

}
