package online.partyrun.partyrunapplication.core.network

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import online.partyrun.partyrunapplication.core.model.SignInTokenResponse
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.TokenExpirationNotifier
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
    private val context: Context,
    private val tokenExpirationNotifier: TokenExpirationNotifier
): Authenticator {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.tag("Authenticate()").e("newAccessToken")
        val refreshToken = runBlocking(Dispatchers.IO) {
            tokenManager.getRefreshToken().first()
        }

        return runBlocking(Dispatchers.IO) {
            val newAccessToken = getNewAccessToken(refreshToken)
            /* Refresh Token 만료 시 */
            if (!newAccessToken.isSuccessful || newAccessToken.body() == null) {
                googleAuthUiClient.signOutGoogleAuth() // Google 로그아웃
                tokenManager.deleteAccessToken()
                // Token 만료 알림 -> 이벤트 브로드캐스팅
                tokenExpirationNotifier.onTokenExpired()
                return@runBlocking null
            }else {
                /* 정상적으로 Access Token을 받아온 경우 */
                newAccessToken.body()?.let {
                    tokenManager.saveAccessToken(it.accessToken)
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${it.accessToken}")
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

        val service = retrofit.create(online.partyrun.partyrunapplication.core.network.service.SignInApiService::class.java)
        return service.replaceToken("Bearer $refreshToken")
    }
}
