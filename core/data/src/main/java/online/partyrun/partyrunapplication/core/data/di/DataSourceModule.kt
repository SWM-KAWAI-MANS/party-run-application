package online.partyrun.partyrunapplication.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import online.partyrun.partyrunapplication.core.data.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.data.datasource.MatchDataSourceImpl
import online.partyrun.partyrunapplication.core.data.repository.MatchRepositoryImpl
import online.partyrun.partyrunapplication.core.network.di.SSEOkHttpClient
import online.partyrun.partyrunapplication.core.network.di.SSERequestBuilder
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
import online.partyrun.partyrunapplication.core.network.service.WaitingBattleApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMatchDataSource(
        @SSEOkHttpClient okHttpClient: OkHttpClient,
        @SSERequestBuilder request: Request.Builder,
        waitingBattleApiService: WaitingBattleApiService,
        matchDecisionApiService: MatchDecisionApiService
    ): MatchDataSource {
        return MatchDataSourceImpl(okHttpClient, request, waitingBattleApiService, matchDecisionApiService)
    }

}
