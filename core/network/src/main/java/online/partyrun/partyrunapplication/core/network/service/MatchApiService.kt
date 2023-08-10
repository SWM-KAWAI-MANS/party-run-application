package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.MatchInfoResponse
import retrofit2.http.GET

interface MatchApiService {

    @GET("/api/matching/recent/members")
    suspend fun getRunnerIds(): ApiResult<MatchInfoResponse>

}
