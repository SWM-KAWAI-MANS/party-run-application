package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.MultipartBody
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.UserDataRequest
import online.partyrun.partyrunapplication.core.network.model.response.BattleMembersInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.DeleteAccountResponse
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse
import online.partyrun.partyrunapplication.core.network.service.MemberApiService
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApiService
) : MemberDataSource {
    override suspend fun getUserData(): ApiResponse<UserResponse> =
        memberApi.getUserData()

    override suspend fun getRunnersInfo(runnerIds: List<String>): ApiResponse<BattleMembersInfoResponse> =
        memberApi.getRunnersInfo(runnerIds)

    override suspend fun deleteAccount(): ApiResponse<DeleteAccountResponse> =
        memberApi.deleteAccount()

    override suspend fun updateUserData(userData: UserDataRequest): ApiResponse<Unit> =
        memberApi.updateUserData(userData)

    override suspend fun updateProfileImage(image: MultipartBody.Part): ApiResponse<Unit> =
        memberApi.updateProfileImage(image)

}
