package online.partyrun.partyrunapplication.core.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.signin.SignInTokenResult
import online.partyrun.partyrunapplication.core.network.api_call_adapter.ApiResultCallAdapterFactory
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class SignInRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: SignInApiService
    private lateinit var repository: SignInRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(
                OkHttpClient.Builder()
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResultCallAdapterFactory.create())
            .build()
            .create(SignInApiService::class.java)
        repository = SignInRepositoryImpl(SignInDataSource(service))
    }

    @Test
    fun `Login Success, api return 201 created`() = runTest {
        val tokenSet = SignInTokenResult(
            accessToken = "access token test",
            refreshToken = "refresh token test"
        )

        val idToken = GoogleIdToken(
            idToken = "idToken test"
        )

        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody(Gson().toJson(tokenSet))
        mockWebServer.enqueue(mockResponse)


        val actualResponse = repository.signInGoogleTokenToServer(idToken).last()
        assertEquals(ApiResponse.Success(tokenSet), actualResponse)
    }

    @Test
    fun `for invalid token, api return 400 code`() = runTest {
        val idToken = GoogleIdToken(
            idToken = null
        )
        val mockResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(mockResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken).last()
        assertTrue(actualResponse is ApiResponse.Failure && actualResponse.code == 400)
    }

    @Test
    fun `for token expired, api return 401 code`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "Expired Tokens"
        )
        val mockResponse = MockResponse()
            .setResponseCode(401)
        mockWebServer.enqueue(mockResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken).last()
        assertTrue(actualResponse is ApiResponse.Failure && actualResponse.code == 401)
    }

    @Test
    fun `for server error, api return 5xx`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "5xx"
        )
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(mockResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken).last()
        assertTrue(actualResponse is ApiResponse.Failure && actualResponse.code == HttpURLConnection.HTTP_INTERNAL_ERROR)
    }
}