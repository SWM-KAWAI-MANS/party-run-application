package online.partyrun.partyrunapplication.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.core.database.PartyRunDatabase
import online.partyrun.partyrunapplication.core.database.dao.BattleRunningHistoryDao
import online.partyrun.partyrunapplication.core.database.dao.SingleRunningHistoryDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesSingleRunningHistoryDao(
        database: PartyRunDatabase,
    ): SingleRunningHistoryDao = database.singleRunningHistoryDao()

    @Provides
    fun providesBattleRunningHistoryDao(
        database: PartyRunDatabase,
    ): BattleRunningHistoryDao = database.battleRunningHistoryDao()

}
