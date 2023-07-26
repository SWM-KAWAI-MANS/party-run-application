package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import retrofit2.http.Body
import retrofit2.http.POST

interface WaitingMatchApiService {
    @POST("/api/waiting")
    suspend fun registerMatch(
        @Body body: UserSelectedMatchDistance
    ): ApiResult<MatchStatusResult>
}
