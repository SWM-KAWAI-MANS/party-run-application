package online.partyrun.partyrunapplication.core.testing.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.model.user.User

class TestMemberRepository : MemberRepository {
    private val user = MutableSharedFlow<ApiResponse<User>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private var userId: String? = null
    private var userName: String? = null
    private var userProfile: String? = null

    override val userData: Flow<User>
        get() = flowOf(User(id = userId ?: "", name = userName ?: "", profile = userProfile ?: ""))

    override suspend fun getUserData(): Flow<ApiResponse<User>> = user

    override suspend fun setUserName(userName: String) {
        this.userName = userName
    }

    override suspend fun setUserProfile(userProfile: String) {
        this.userProfile = userProfile
    }

    override suspend fun setUserId(userId: String) {
        this.userId = userId
    }
}
