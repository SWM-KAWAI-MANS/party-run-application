package online.partyrun.partyrunapplication.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSourceImpl
import online.partyrun.partyrunapplication.core.network.di.SSEOkHttpClient
import online.partyrun.partyrunapplication.core.network.di.SSERequestBuilder
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
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

}
