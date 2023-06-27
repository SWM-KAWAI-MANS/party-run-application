package online.partyrun.partyrunapplication.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.model.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.SignInTokenResponse
import online.partyrun.partyrunapplication.core.network.api_call_adapter.ApiResultCallAdapterFactory
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import org.junit.Assert.fail
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
        val tokenSet = SignInTokenResponse(
            accessToken = "access token test",
            refreshToken = "refresh token test"
        )

        val idToken = GoogleIdToken(
            idToken = "idToken test"
        )

        var expectedResponse = MockResponse()
            .setResponseCode(201)
            .setBody(Gson().toJson(tokenSet))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken)
        actualResponse.collect() {
            when (it) {
                is ApiResponse.Success -> {
                    assertThat(it.data).isEqualTo(tokenSet)
                }
                is ApiResponse.Failure -> {
                    fail("Expected Success, got Failure")
                }
                else -> {}
            }
        }
    }

    @Test
    fun `for invalid token, api return 400 code`() = runTest {
        val idToken = GoogleIdToken(
            idToken = null
        )
        val expectedResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken)
        actualResponse.collect() {
            when (it) {
                is ApiResponse.Success -> {
                    fail("Expected Failure, got Success")
                }
                is ApiResponse.Failure -> {
                    assertThat(it.code).isEqualTo(400)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `for token expired, api return 401 code`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "Expired Tokens"
        )
        val expectedResponse = MockResponse()
            .setResponseCode(401)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken)
        actualResponse.collect() {
            when (it) {
                is ApiResponse.Success -> {
                    fail("Expected Failure, got Success")
                }
                is ApiResponse.Failure -> {
                    assertThat(it.code).isEqualTo(401)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `for server error, api return 5xx`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "5xx"
        )
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.signInGoogleTokenToServer(idToken)
        actualResponse.collect() {
            when (it) {
                is ApiResponse.Success -> {
                    fail("Expected Failure, got Success")
                }
                is ApiResponse.Failure -> {
                    assertThat(it.code).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR)
                }
                else -> {}
            }
        }
    }

}
