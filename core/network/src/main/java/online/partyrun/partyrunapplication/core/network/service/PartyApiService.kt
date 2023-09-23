package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.PartyCodeResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PartyApiService {
    @POST("/api/parties")
    suspend fun createParty(
        @Body body: RunningDistanceRequest
    ): ApiResponse<PartyCodeResponse>

    @POST("/api/parties/{code}/start")
    suspend fun startPartyBattle(
        @Path("code") code: String
    ): ApiResponse<Unit>

}
