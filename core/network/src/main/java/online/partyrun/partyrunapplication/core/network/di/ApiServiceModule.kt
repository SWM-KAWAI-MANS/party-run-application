package online.partyrun.partyrunapplication.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import online.partyrun.partyrunapplication.core.network.service.ResultApiService
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
import online.partyrun.partyrunapplication.core.network.service.MemberApiService
import online.partyrun.partyrunapplication.core.network.service.BattleApiService
import online.partyrun.partyrunapplication.core.network.service.MatchApiService
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import online.partyrun.partyrunapplication.core.network.service.SingleApiService
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

    @Singleton
    @Provides
    fun provideResultApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): ResultApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(ResultApiService::class.java)

    @Singleton
    @Provides
    fun provideMemberApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): MemberApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(MemberApiService::class.java)

    @Provides
    fun provideBattleApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): BattleApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(BattleApiService::class.java)

    @Provides
    fun provideSingleApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): SingleApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SingleApiService::class.java)

    @Singleton
    @Provides
    fun provideMatchApiService(@RESTOkHttpClient okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): MatchApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(MatchApiService::class.java)

}
