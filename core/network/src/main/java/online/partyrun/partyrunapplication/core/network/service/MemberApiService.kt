package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse
import retrofit2.http.GET

interface MemberApiService {
    @GET("/api/members/me")
    suspend fun getUserData(): ApiResult<UserResponse>

}
