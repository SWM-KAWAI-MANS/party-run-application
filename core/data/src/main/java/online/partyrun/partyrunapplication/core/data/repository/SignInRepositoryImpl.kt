package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.signin.SignInToken
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInDataSource: SignInDataSource
) : SignInRepository {
    override suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken): Flow<ApiResponse<SignInToken>> {
        return apiRequestFlow { signInDataSource(idToken.toRequestModel()) }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }

}
