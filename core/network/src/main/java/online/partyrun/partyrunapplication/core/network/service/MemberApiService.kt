package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.UserDataRequest
import online.partyrun.partyrunapplication.core.network.model.response.BattleMembersInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.DeleteAccountResponse
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface MemberApiService {
    @GET("/api/members/me")
    suspend fun getUserData(): ApiResponse<UserResponse>

    @GET("api/members")
    suspend fun getRunnersInfo(@Query("ids") runnerIds: List<String>): ApiResponse<BattleMembersInfoResponse>

    @DELETE("/api/members/me")
    suspend fun deleteAccount(): ApiResponse<DeleteAccountResponse>

    @PATCH("/api/members/name")
    suspend fun updateUserData(@Body userData: UserDataRequest): ApiResponse<Unit>
}
