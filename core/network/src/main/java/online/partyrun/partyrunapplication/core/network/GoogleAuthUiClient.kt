package online.partyrun.partyrunapplication.core.network

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import online.partyrun.partyrunapplication.core.common.Constants.FB_GOOGLE_WEB_CLIENT_ID
import online.partyrun.partyrunapplication.core.model.SignInGoogleResult
import online.partyrun.partyrunapplication.core.model.UserGoogleData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  GoogleAuthUiClient Class: Google 로그인 동작 수행
 *  Firebase 인증 및 Google One-Tap API 사용 -> 사용자 로그인 및 로그아웃 로직 처리
 */
@Singleton
class GoogleAuthUiClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val oneTapClient: SignInClient, // Google One-Tap 로그인을 위한 SignInClient 객체
) {
    private val auth = Firebase.auth // Firebase 인증을 위한 auth 객체

    /**
     * signInGoogle(): 로그인 성공 시 IntentSender 반환
     * oneTapClient의 beginSignIn() 호출 -> 로그인 프로세스 동작 수행
     * buildSignInRequest() 호출 -> 로그인 요청을 구성하고, 결과로 받은 PendingIntent의 intentSender 반환
     */
    suspend fun signInGoogle(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInGoogleRequest()
            ).await()
        } catch(e: Exception) {
            Timber.tag("GoogleAuthUiClient").e(e, "signInGoogle()")
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    /**
     * buildSignInGoogleRequest(): One-Tap 로그인을 위한 BeginSignInRequest 객체 구성
     * GoogleIdTokenRequestOptions를 설정하여 구글 IdToken 요청 옵션을 지정하고,
     * BeginSignInRequest.Builder()를 통해 로그인 요청 구성
     */
    private fun buildSignInGoogleRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(FB_GOOGLE_WEB_CLIENT_ID) // Constants FB_GOOGLE_WEB_CLIENT_ID
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    /**
     * signInGoogleWithIntent(): Intent를 매개변수로 받아 사용자 인증 -> 로그인 결과를 SignInResult 객체로 반환
     * oneTapClient의 getSignInCredentialFromIntent() -> Intent에서 로그인 자격 증명 추출
     * 추출한 자격 증명으로 GoogleAuthProvider를 사용하여 Firebase에 대한 자격 증명 생성
     * auth 객체의 signInWithCredential() 호출해 사용자 인증 -> val user = ~
     */
    suspend fun signInGoogleWithIntent(
        intent: Intent,
        signInGoogleLoadingIndicator: () -> Unit
    ): SignInGoogleResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        signInGoogleLoadingIndicator()
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInGoogleResult(
                userData = user?.run {
                    UserGoogleData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            Timber.tag("GoogleAuthUiClient").e(e, "Sign-in with Google failed")
            if(e is CancellationException) throw e
            SignInGoogleResult(
                userData = null,
                errorMessage = e.message
            )
        }
    }

    /**
     * signOutGoogleAuth(): 사용자 구글 로그아웃 동작 수행
     * oneTapClient의 signOut() 호출 -> One-Tap 로그인 해제
     * auth 객체의 signOut() 호출 -> Firebase 인증 로그아웃
     */
    suspend fun signOutGoogleAuth() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            Timber.tag("GoogleAuthUiClient").e(e, "signOutGoogleAuth()")
            if(e is CancellationException) throw e
        }
    }

    /**
     * getGoogleAuthUser(): 현재 구글에 로그인된 사용자 정보를 UserData 객체로 반환
     * 현재 사용자가 로그인되어 있지 않으면 null 반환
     */
    fun getGoogleAuthUser(): UserGoogleData? = auth.currentUser?.run {
        UserGoogleData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }
}
