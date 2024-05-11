package online.partyrun.partyrunapplication.core.testing.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import okhttp3.RequestBody
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.user.DeleteAccount
import online.partyrun.partyrunapplication.core.model.user.User

class TestMemberRepository : MemberRepository {
    private val user =
        MutableSharedFlow<Result<User>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private var userId: String? = null
    private var userName: String? = null
    private var userProfile: String? = null

    override val userData: Flow<User>
        get() = flowOf(
            User(
                id = userId ?: "",
                nickName = userName ?: "",
                profileImage = userProfile ?: ""
            )
        )

    override suspend fun getRunnersInfo(runnerIds: RunnerIds): Result<RunnerInfoData> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserData(): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(): Result<DeleteAccount> {
        TODO("Not yet implemented")
    }

    override suspend fun setUserName(userName: String) {
        this.userName = userName
    }

    override suspend fun setUserProfile(userProfile: String) {
        this.userProfile = userProfile
    }

    override suspend fun setUserId(userId: String) {
        this.userId = userId
    }

    override suspend fun updateUserData(userData: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfileImage(
        requestBody: RequestBody,
        fileName: String?,
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

}
