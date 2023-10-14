package online.partyrun.partyrunapplication.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.core.database.PartyRunDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePartyRunDatabase(@ApplicationContext context: Context): PartyRunDatabase =
        Room.databaseBuilder(
            context,
            PartyRunDatabase::class.java,
            "party-run-database"
        ).build()

}
