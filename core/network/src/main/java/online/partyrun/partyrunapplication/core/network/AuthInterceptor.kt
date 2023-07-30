package online.partyrun.partyrunapplication.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import online.partyrun.partyrunapplication.core.datastore.datasource.TokenDataSource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor를 구현하여 HTTP 요청에 대한 인증 토큰을 추가
 * HTTP 요청을 가로채고 처리하는 동작 수행
 * 인증 토큰을 request에 추가하여 네트워크 요청 처리
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking(Dispatchers.IO) {
            tokenDataSource.getAccessToken().first() // 액세스 토큰 값을 Flow 형태로 반환 -> first(): Flow의 첫 번째 값을 동기적으로 반환
        }
        Timber.tag("AuthInterceptor").d("$accessToken")
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder() //  현재 request에 대한 빌더 생성
        requestBuilder.addHeader("Authorization", "$accessToken") // 빌더에 Authorization 헤더 추가해 토큰 포함
        val request = requestBuilder.build()
        return chain.proceed(request) // 변경된 request를 다음 인터셉터로 전달, response 반환
    }
}
