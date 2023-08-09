package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.response.UserResponse
import online.partyrun.partyrunapplication.core.network.service.MemberApiService
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApiService
) : MemberDataSource{
    override suspend fun getUserData(): ApiResult<UserResponse> =
        memberApi.getUserData()

}
