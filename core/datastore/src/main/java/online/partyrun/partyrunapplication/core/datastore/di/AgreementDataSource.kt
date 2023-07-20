package online.partyrun.partyrunapplication.core.datastore.di

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AgreementDataSource(
    private val context: Context
) {
    companion object {
        private val AGREEMENT_KEY = booleanPreferencesKey("terms_conditions_agree")
    }

    suspend fun saveAgreementState(checked: Boolean) {
        context.agreementDataStore.edit { preferences ->
            preferences[AGREEMENT_KEY] = checked
        }
    }

    fun getAgreementState(): Flow<Boolean> {
        return context.agreementDataStore.data
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
