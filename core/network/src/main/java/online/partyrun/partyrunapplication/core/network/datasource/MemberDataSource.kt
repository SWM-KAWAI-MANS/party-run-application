package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse

interface MemberDataSource {
    suspend fun getUserData(): ApiResult<UserResponse>

}
