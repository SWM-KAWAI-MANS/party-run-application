package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.datastore.datasource.UserPreferencesDataSource
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.user.DeleteAccount
import online.partyrun.partyrunapplication.core.model.user.User
import online.partyrun.partyrunapplication.core.network.datasource.MemberDataSource
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val memberDataSource: MemberDataSource
) : MemberRepository {

    override val userData: Flow<User> =
        userPreferencesDataSource.userData

    override suspend fun getRunnersInfo(runnerIds: RunnerIds): Flow<ApiResponse<RunnerInfoData>> {
        return apiRequestFlow { memberDataSource.getRunnersInfo(runnerIds.runnerIds) } //  // runnerIds를 List<String>으로 변환하고 쿼리스트링 전달
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(
                        apiResponse.errorMessage,
                        apiResponse.code
                    )
                }
            }
    }

    override suspend fun getUserData(): Flow<ApiResponse<User>> {
        return apiRequestFlow { memberDataSource.getUserData() }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(
                        apiResponse.errorMessage,
                        apiResponse.code
                    )
                }
            }
    }

    override suspend fun deleteAccount(): Flow<ApiResponse<DeleteAccount>> {
        return apiRequestFlow { memberDataSource.deleteAccount() }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(
                        apiResponse.errorMessage,
                        apiResponse.code
                    )
                }
            }
    }

    override suspend fun setUserName(userName: String) {
        userPreferencesDataSource.setUserName(userName)
    }

    override suspend fun setUserProfile(userProfile: String) {
        userPreferencesDataSource.setUserProfile(userProfile)
    }

    override suspend fun setUserId(userId: String) {
        userPreferencesDataSource.setUserId(userId)
    }

}
