package online.partyrun.partyrunapplication.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import online.partyrun.partyrunapplication.core.network.service.WaitingMatchApiService
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
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

    @Singleton
    @Provides
    fun provideWaitingMatchApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Builder): WaitingMatchApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(WaitingMatchApiService::class.java)

    @Singleton
    @Provides
    fun provideMatchDecisionApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Builder): MatchDecisionApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(MatchDecisionApiService::class.java)

}
