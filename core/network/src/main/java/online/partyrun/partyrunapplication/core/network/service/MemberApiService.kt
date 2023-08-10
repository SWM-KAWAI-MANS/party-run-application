package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.BattleMembersInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MemberApiService {
    @GET("/api/members/me")
    suspend fun getUserData(): ApiResult<UserResponse>

    @GET("api/members")
    suspend fun getRunnersInfo(@Query("ids") runnerIds: List<String>): ApiResult<BattleMembersInfoResponse>

}
