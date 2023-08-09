package online.partyrun.partyrunapplication.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import online.partyrun.partyrunapplication.core.network.datasource.RunningResultDataSource
import online.partyrun.partyrunapplication.core.network.datasource.RunningResultDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.MemberDataSource
import online.partyrun.partyrunapplication.core.network.datasource.MemberDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSourceImpl
import online.partyrun.partyrunapplication.core.network.service.BattleResultApiService
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
import online.partyrun.partyrunapplication.core.network.service.MemberApiService
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import online.partyrun.partyrunapplication.core.network.service.WaitingMatchApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMatchDataSource(
        @SSEOkHttpClient okHttpClient: OkHttpClient,
        @SSERequestBuilder request: Request.Builder,
        waitingMatchApiService: WaitingMatchApiService,
        matchDecisionApiService: MatchDecisionApiService
    ): MatchDataSource {
        return MatchDataSourceImpl(okHttpClient, request, waitingMatchApiService, matchDecisionApiService)
    }

    @Singleton
    @Provides
    fun provideSignInDataSource(
        signInApiService: SignInApiService
    ): SignInDataSource = SignInDataSourceImpl(signInApiService)

    @Singleton
    @Provides
    fun provideRunningResultDataSource(
        battleResultApiService: BattleResultApiService
    ): RunningResultDataSource = RunningResultDataSourceImpl(battleResultApiService)

    @Singleton
    @Provides
    fun provideMemberDataSource(
        memberApiService: MemberApiService
    ): MemberDataSource = MemberDataSourceImpl(memberApiService)

}
