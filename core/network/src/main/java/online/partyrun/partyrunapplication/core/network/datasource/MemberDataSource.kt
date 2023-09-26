package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.MultipartBody
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.UserDataRequest
import online.partyrun.partyrunapplication.core.network.model.response.BattleMembersInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.DeleteAccountResponse
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse

interface MemberDataSource {
    suspend fun getUserData(): ApiResponse<UserResponse>
    suspend fun getRunnersInfo(runnerIds: List<String>): ApiResponse<BattleMembersInfoResponse>
    suspend fun deleteAccount(): ApiResponse<DeleteAccountResponse>
    suspend fun updateUserData(userData: UserDataRequest): ApiResponse<Unit>
    suspend fun updateProfileImage(image: MultipartBody.Part): ApiResponse<Unit>
}
