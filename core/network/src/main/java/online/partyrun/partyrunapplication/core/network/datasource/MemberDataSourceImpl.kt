package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleMembersInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.DeleteAccountResponse
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse
import online.partyrun.partyrunapplication.core.network.service.MemberApiService
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApiService
) : MemberDataSource{
    override suspend fun getUserData(): ApiResponse<UserResponse> =
        memberApi.getUserData()

    override suspend fun getRunnersInfo(runnerIds: List<String>): ApiResponse<BattleMembersInfoResponse> =
        memberApi.getRunnersInfo(runnerIds)

    override suspend fun deleteAccount(): ApiResponse<DeleteAccountResponse> =
        memberApi.deleteAccount()

}
