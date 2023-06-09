package online.partyrun.partyrunapplication.core.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.MatchSourceManager
import online.partyrun.partyrunapplication.core.common.network.TokenExpirationNotifier
import online.partyrun.partyrunapplication.core.network.AuthAuthenticator
import online.partyrun.partyrunapplication.core.network.AuthInterceptor
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import online.partyrun.partyrunapplication.core.network.MatchSourceManagerImpl
import online.partyrun.partyrunapplication.core.network.RealtimeBattleClient
import online.partyrun.partyrunapplication.core.network.TokenManager
import online.partyrun.partyrunapplication.core.network.api_call_adapter.ApiResultCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

val Context.accessDataStore: DataStore<Preferences> by preferencesDataStore(name = "access_token")
val Context.refreshDataStore: DataStore<Preferences> by preferencesDataStore(name = "refresh_token")
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        tokenManager: TokenManager,
        @ApplicationContext context: Context,
        tokenExpirationNotifier: TokenExpirationNotifier
    ): AuthAuthenticator =
        AuthAuthenticator(tokenManager, context, tokenExpirationNotifier)

    /* SignInClient 인스턴스 생성 */
    @Provides
    fun provideOneTapClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context,
        oneTapClient: SignInClient
    ): GoogleAuthUiClient =
        GoogleAuthUiClient(context, oneTapClient)

    @Provides
    fun provideMatchSourceManager(): MatchSourceManager {
        return MatchSourceManagerImpl()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResultCallAdapterFactory.create()) // 빌더에 ApiResultCallAdapterFactory 적용

    @RESTOkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @SSEOkHttpClient
    @Provides
    @Singleton
    fun provideSSEOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .addInterceptor(authInterceptor)
        .build()

    @WSOkHttpClient
    @Singleton
    @Provides
    fun provideWSOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .addInterceptor(authInterceptor)
        .authenticator(authAuthenticator)
        .build()

    @Singleton
    @Provides
    fun provideRealtimeBattleClient(@WSOkHttpClient httpClient: OkHttpClient): RealtimeBattleClient {
        return RealtimeBattleClient(httpClient)
    }

    @SSERequestBuilder
    @Provides
    @Singleton
    fun provideRequestBuilder(): Request.Builder =
        Request.Builder()
            .addHeader("Content-Type", "text/event-stream")


}
