package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.user.DeleteAccount
import online.partyrun.partyrunapplication.core.model.user.User

interface MemberRepository {

    val userData: Flow<User>

    suspend fun getRunnersInfo(runnerIds: RunnerIds): Flow<ApiResponse<RunnerInfoData>>

    suspend fun getUserData(): Flow<ApiResponse<User>>

    suspend fun deleteAccount(): Flow<ApiResponse<DeleteAccount>>

    suspend fun setUserName(userName: String)

    suspend fun setUserProfile(userProfile: String)

    suspend fun setUserId(userId: String)
}
