package online.partyrun.partyrunapplication.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.agreementDataStore: DataStore<Preferences> by preferencesDataStore(name = "terms_conditions_pref")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideAgreementDataSource(
        @ApplicationContext context: Context
    ): AgreementDataSource = AgreementDataSource(context)
}
