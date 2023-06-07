package online.partyrun.partyrunapplication.di.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import online.partyrun.partyrunapplication.data.model.SignInTokenResponse
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.network.ApiResult
import online.partyrun.partyrunapplication.network.BaseResponse
import online.partyrun.partyrunapplication.network.apiRequestFlow
import online.partyrun.partyrunapplication.network.service.SignInApiService
import online.partyrun.partyrunapplication.utils.Constants.SERVER_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Authenticator
 * requestCode가 401 코드가 포함된 응답을 반환받았을 때 authenticate() 호출
 */
@Singleton
class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        val refreshToken = runBlocking(Dispatchers.IO) {
            tokenManager.getRefreshToken().first()
        }

        return runBlocking(Dispatchers.IO) {
            lateinit var newAccessTokenData: String
            //lateinit var newRefreshTokenData: String?

            val newAccessToken = apiRequestFlow {
                getNewAccessToken(refreshToken)
            }
            newAccessToken.collect() {
                when(it) {
                    is ApiResponse.Success -> {
                        tokenManager.deleteAccessToken()
                        newAccessTokenData = it.data.data.accessToken
                        //newRefreshTokenData = it.data.data.refreshToken
                    }
                    is ApiResponse.Failure -> {
                        Timber.tag("AuthAuthenticator").e("${it.code} ${it.errorMessage}")
                    }
                    ApiResponse.Loading ->  {
                        Timber.tag("AuthAuthenticator").e("Loading")
                    }
                }
            }
            tokenManager.saveAccessToken(newAccessTokenData)
            //tokenManager.saveRefreshToken(newRefreshTokenData)
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessTokenData")
                .build()
        }
    }

    private suspend fun getNewAccessToken(refreshToken: String?): ApiResult<BaseResponse<SignInTokenResponse>> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL) /* SERVER BASE URL */
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(SignInApiService::class.java)
        return service.replaceToken("Bearer $refreshToken")
    }
}
