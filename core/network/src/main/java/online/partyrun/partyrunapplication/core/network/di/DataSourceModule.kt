package online.partyrun.partyrunapplication.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import online.partyrun.partyrunapplication.core.network.datasource.BattleDataSource
import online.partyrun.partyrunapplication.core.network.datasource.BattleDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.ResultDataSource
import online.partyrun.partyrunapplication.core.network.datasource.ResultDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.MemberDataSource
import online.partyrun.partyrunapplication.core.network.datasource.MemberDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.PartyDataSource
import online.partyrun.partyrunapplication.core.network.datasource.PartyDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSourceImpl
import online.partyrun.partyrunapplication.core.network.datasource.SingleDataSource
import online.partyrun.partyrunapplication.core.network.datasource.SingleDataSourceImpl
import online.partyrun.partyrunapplication.core.network.service.BattleApiService
import online.partyrun.partyrunapplication.core.network.service.MatchApiService
import online.partyrun.partyrunapplication.core.network.service.ResultApiService
import online.partyrun.partyrunapplication.core.network.service.MemberApiService
import online.partyrun.partyrunapplication.core.network.service.PartyApiService
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import online.partyrun.partyrunapplication.core.network.service.SingleApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMatchDataSource(
        @SSEOkHttpClient okHttpClient: OkHttpClient,
        @SSERequestBuilder request: Request.Builder,
        matchApiService: MatchApiService
    ): MatchDataSource {
        return MatchDataSourceImpl(
            okHttpClient,
            request,
            matchApiService
        )
    }

    @Singleton
    @Provides
    fun provideSignInDataSource(
        signInApiService: SignInApiService
    ): SignInDataSource = SignInDataSourceImpl(signInApiService)

    @Singleton
    @Provides
    fun provideResultDataSource(
        resultApiService: ResultApiService
    ): ResultDataSource = ResultDataSourceImpl(resultApiService)

    @Singleton
    @Provides
    fun provideMemberDataSource(
        memberApiService: MemberApiService
    ): MemberDataSource = MemberDataSourceImpl(memberApiService)

    @Singleton
    @Provides
    fun provideBattleDataSource(
        battleApiService: BattleApiService
    ): BattleDataSource = BattleDataSourceImpl(battleApiService)

    @Singleton
    @Provides
    fun provideSingleDataSource(
        singleApiService: SingleApiService
    ): SingleDataSource = SingleDataSourceImpl(singleApiService)

    @Singleton
    @Provides
    fun providePartyDataSource(
        @SSEOkHttpClient okHttpClient: OkHttpClient,
        @SSERequestBuilder request: Request.Builder,
        partyApiService: PartyApiService
    ): PartyDataSource = PartyDataSourceImpl(
        okHttpClient,
        request,
        partyApiService
    )

}
