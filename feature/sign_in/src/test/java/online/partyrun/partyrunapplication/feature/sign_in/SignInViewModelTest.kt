package online.partyrun.partyrunapplication.feature.sign_in

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.SendSignInIdTokenUseCase
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.signin.SignInTokenResult
import online.partyrun.partyrunapplication.core.network.TokenManager
import online.partyrun.partyrunapplication.core.testing.repository.TestSignInRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class SignInViewModelTest {

    private val signInRepository = TestSignInRepository()
    private val tokenManager = mock(TokenManager::class.java)
    private val signInUseCase = SendSignInIdTokenUseCase(
        signInRepository = signInRepository
    )

    private lateinit var viewModel: SignInViewModel

    @get:Rule
    val dispatcherRule = ViewModelRule()

    @Before
    fun setUp() {
        viewModel = SignInViewModel(
            sendSignInIdTokenUseCase = signInUseCase,
            tokenManager = tokenManager
        )
    }

    @Test
    fun `for valid ID token, sendIdTokenToServer should be true`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "valid token value"
        )
        val response = SignInTokenResult("test", "test")
        signInRepository.emit(ApiResponse.Success(response))
        viewModel.signInGoogleTokenToServer(idToken)
        signInUseCase(idToken)
        val data = viewModel.signInGoogleState.value
        assertThat(data.sendIdTokenToServer).isTrue()
    }

    @Test
    fun `for invalid ID token, isSignInSuccessful should be false`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "invalid"
        )
        signInRepository.emit(ApiResponse.Failure("잘못된 요청입니다.", 400))
        viewModel.signInGoogleTokenToServer(idToken)
        signInUseCase(idToken)
        val data = viewModel.signInGoogleState.value
        assertThat(data.isSignInSuccessful).isFalse()
    }

}
