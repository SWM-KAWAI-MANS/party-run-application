package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleMembersInfoResponse
import online.partyrun.partyrunapplication.core.network.model.response.DeleteAccountResponse
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse

interface MemberDataSource {
    suspend fun getUserData(): ApiResponse<UserResponse>
    suspend fun getRunnersInfo(runnerIds: List<String>): ApiResponse<BattleMembersInfoResponse>
    suspend fun deleteAccount(): ApiResponse<DeleteAccountResponse>
}
