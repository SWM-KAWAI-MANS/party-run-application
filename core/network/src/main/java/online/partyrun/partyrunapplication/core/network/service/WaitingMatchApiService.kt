package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface WaitingMatchApiService {
    @POST("/api/waiting")
    suspend fun registerMatch(
        @Body body: RunningDistanceRequest
    ): ApiResult<MatchStatusResponse>
}

