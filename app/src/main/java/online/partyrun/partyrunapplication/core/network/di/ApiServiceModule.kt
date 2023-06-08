package online.partyrun.partyrunapplication.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import online.partyrun.partyrunapplication.core.network.service.TestApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    /* Interceptor를 붙이지 않을 경우, okHttpClient 넣지 않고 Retrofit build 수행*/
    @Singleton
    @Provides
    fun provideSignInApiService(retrofit: Retrofit.Builder): SignInApiService =
        retrofit
            .build()
            .create(SignInApiService::class.java)

    /*
    @Singleton
    @Provides
    fun provideSignInApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): SignInApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SignInApiService::class.java)
     */

    @Provides
    @Singleton
    fun provideTestApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): TestApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(TestApiService::class.java)
}
