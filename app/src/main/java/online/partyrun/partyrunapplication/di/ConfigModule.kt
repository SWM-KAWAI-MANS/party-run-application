package online.partyrun.partyrunapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.partyrun.partyrunapplication.network.apiCallAdapter.ApiResultCallAdapterFactory
import online.partyrun.partyrunapplication.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_store")

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    //@Singleton
    //@Provides
    //fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    /*
    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)
    */

    /*
    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

     */

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResultCallAdapterFactory.create()) // 빌더에 ApiResultCallAdapterFactory 적용

    @Singleton
    @Provides
    fun provideOkHttpClient(
        // authInterceptor: AuthInterceptor,
        // authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            //.addNetworkInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            //.authenticator(authAuthenticator)
            .build()
    }
}
