package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.user.DeleteAccount
import online.partyrun.partyrunapplication.core.model.user.User

interface MemberRepository {

    val userData: Flow<User>

    suspend fun getRunnersInfo(runnerIds: RunnerIds): Flow<Result<RunnerInfoData>>

    suspend fun getUserData(): Flow<Result<User>>

    suspend fun deleteAccount(): Flow<Result<DeleteAccount>>

    suspend fun setUserName(userName: String)

    suspend fun setUserProfile(userProfile: String)

    suspend fun setUserId(userId: String)

    suspend fun updateUserData(userData: User): Flow<Result<Unit>>

    suspend fun updateProfileImage(requestBody: RequestBody, fileName: String?): Flow<Result<Unit>>
}
