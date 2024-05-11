package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import online.partyrun.partyrunapplication.core.datastore.datasource.UserPreferencesDataSource
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.user.DeleteAccount
import online.partyrun.partyrunapplication.core.model.user.User
import online.partyrun.partyrunapplication.core.network.datasource.MemberDataSource
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.toResultModel
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val memberDataSource: MemberDataSource,
) : MemberRepository {

    override val userData: Flow<User> =
        userPreferencesDataSource.userData

    override suspend fun getRunnersInfo(runnerIds: RunnerIds): Result<RunnerInfoData> {
        return memberDataSource
            .getRunnersInfo(runnerIds.runnerIds)  // runnerIds를 List<String>으로 변환하고 쿼리스트링 전달
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun getUserData(): Result<User> {
        return memberDataSource
            .getUserData()
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun deleteAccount(): Result<DeleteAccount> {
        return memberDataSource
            .deleteAccount()
            .toResultModel { it.toDomainModel() }
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

    override suspend fun updateUserData(userData: User): Result<Unit> {
        return memberDataSource.updateUserData(userData.toRequestModel()).toResultModel { }
    }

    override suspend fun updateProfileImage(
        requestBody: RequestBody,
        fileName: String?,
    ): Result<Unit> {
        return memberDataSource
            .updateProfileImage(
                createMultipartBodyPart(
                    fileName,
                    requestBody
                )
            ).toResultModel { }
    }

    private fun createMultipartBodyPart(
        fileName: String?,
        requestBody: RequestBody,
    ): MultipartBody.Part {
        return MultipartBody.Part.createFormData("profile", fileName, requestBody)
    }
}
