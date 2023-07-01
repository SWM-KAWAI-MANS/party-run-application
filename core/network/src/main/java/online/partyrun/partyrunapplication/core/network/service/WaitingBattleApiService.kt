package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import retrofit2.http.Body
import retrofit2.http.POST

interface WaitingBattleApiService {
    @POST("/api/waiting")
    suspend fun registerToBattleMatchingQueue(
        @Body body: UserSelectedMatchDistance
    ): ApiResult<MatchStatusResult>
}
