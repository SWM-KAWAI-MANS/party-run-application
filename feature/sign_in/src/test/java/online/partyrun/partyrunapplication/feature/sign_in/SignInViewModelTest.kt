package online.partyrun.partyrunapplication.feature.sign_in

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.auth.GetSignInTokenUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignInUseCase
import online.partyrun.partyrunapplication.core.domain.auth.SaveTokensUseCase
import online.partyrun.partyrunapplication.core.domain.member.GetUserDataUseCase
import online.partyrun.partyrunapplication.core.domain.member.SaveUserDataUseCase
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import online.partyrun.partyrunapplication.core.testing.repository.TestGoogleAuthRepository
import online.partyrun.partyrunapplication.core.testing.repository.TestMemberRepository
import online.partyrun.partyrunapplication.core.testing.repository.TestSignInRepository
import online.partyrun.partyrunapplication.core.testing.repository.TestTokenRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {

    private val signInRepository = TestSignInRepository()
    private val tokenRepository = TestTokenRepository()
    private val memberRepository = TestMemberRepository()
    private val googleAuthRepository = TestGoogleAuthRepository()
    private val getSignInTokenUseCase = GetSignInTokenUseCase(
        signInRepository = signInRepository
    )
    private val saveTokensUseCase = SaveTokensUseCase(
        tokenRepository = tokenRepository
    )
    private val googleSignInUseCase = GoogleSignInUseCase(
        googleAuthRepository = googleAuthRepository
    )
    private val getUserDataUseCase = GetUserDataUseCase(
        memberRepository = memberRepository
    )
    private val saveUserDataUseCase = SaveUserDataUseCase(
        memberRepository = memberRepository
    )

    private lateinit var viewModel: SignInViewModel

    @get:Rule
    val dispatcherRule = ViewModelRule()

    @Before
    fun setUp() {
        viewModel = SignInViewModel(
            getSignInTokenUseCase = getSignInTokenUseCase,
            saveTokensUseCase = saveTokensUseCase,
            googleSignInUseCase = googleSignInUseCase,
            getUserDataUseCase = getUserDataUseCase,
            saveUserDataUseCase = saveUserDataUseCase
        )
    }

    @Test
    fun `for valid ID token, sendIdTokenToServer should be true`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "valid token value"
        )
        val response = SignInTokenResponse("test", "test")
        signInRepository.emit(ApiResponse.Success(response.toDomainModel()))
        viewModel.signInWithGoogleTokenViaServer(idToken)
        getSignInTokenUseCase(idToken)
        val data = viewModel.signInGoogleState.value
        assertThat(data.isIdTokenSentToServer).isTrue()
    }

    @Test
    fun `for invalid ID token, isSignInSuccessful should be false`() = runTest {
        val idToken = GoogleIdToken(
            idToken = "invalid"
        )
        signInRepository.emit(ApiResponse.Failure("잘못된 요청입니다.", 400))
        viewModel.signInWithGoogleTokenViaServer(idToken)
        getSignInTokenUseCase(idToken)
        val data = viewModel.signInGoogleState.value
        assertThat(data.isSignInSuccessful).isFalse()
    }

}
