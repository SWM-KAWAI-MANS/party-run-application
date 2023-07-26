package online.partyrun.partyrunapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.core.common.network.RefreshTokenExpirationNotifier

@Module
@InstallIn(SingletonComponent::class)
object TokenExpirationModule {

    @Provides
    fun provideRefreshTokenExpirationNotifier(
        @ApplicationContext context: Context
    ): RefreshTokenExpirationNotifier =
        RefreshTokenExpirationNotifier(context)

}
