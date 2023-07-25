package online.partyrun.partyrunapplication.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AgreementDataSource @Inject constructor(
    private val agreementDataStore: DataStore<Preferences>
) {
    companion object {
        private val AGREEMENT_KEY = booleanPreferencesKey("terms_conditions_agree")
    }

    suspend fun saveAgreementState(checked: Boolean) {
        agreementDataStore.edit { preferences ->
            preferences[AGREEMENT_KEY] = checked
        }
    }

    fun getAgreementState(): Flow<Boolean> {
        return agreementDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val agreementState = preferences[AGREEMENT_KEY] ?: false
                agreementState
            }
    }
}
