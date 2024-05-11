package online.partyrun.partyrunapplication.core.network

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.TokenExpirationNotifier
import online.partyrun.partyrunapplication.core.datastore.datasource.TokenDataSource
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
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
    private val tokenDataSource: TokenDataSource,
    private val context: Context,
    private val tokenExpirationNotifier: TokenExpirationNotifier,
) : Authenticator {

    private val googleAuthUiClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.tag("AuthAuthenticator").d("getNewAccessToken")
        val refreshToken = runBlocking(Dispatchers.IO) {
            tokenDataSource.getRefreshToken().first()
        }

        return runBlocking(Dispatchers.IO) {
            val newAccessToken = getNewAccessToken(refreshToken)
            if (!newAccessToken.isSuccessful || newAccessToken.body() == null) {
                /* Refresh Token 만료 시 */
                googleAuthUiClient.signOutGoogleAuth() // Google 로그아웃
                tokenDataSource.deleteAccessToken()
                // Token 만료 알림 -> 이벤트 브로드캐스팅
                tokenExpirationNotifier.notifyRefreshTokenExpired()
                return@runBlocking null
            } else {
                /* 정상적으로 새로운 Token Set을 받아온 경우 */
                newAccessToken.body()?.let {
                    tokenDataSource.saveAccessToken(it.accessToken.orEmpty())
                    tokenDataSource.saveRefreshToken(it.refreshToken.orEmpty())
                    response.request.newBuilder()
                        .header("Authorization", it.accessToken.orEmpty())
                        .build()
                }
            }
        }
    }

    /* 리턴타입에 BaseResponse를 붙이지 않는 경우 */
    private suspend fun getNewAccessToken(refreshToken: String?): retrofit2.Response<SignInTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) /* SERVER BASE URL */
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(SignInApiService::class.java)
        return service.replaceToken("$refreshToken")
    }
}
