package online.partyrun.partyrunapplication.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class SinglePreferencesDataSourceImpl @Inject constructor(
    private val singlePreferencesDataSource: DataStore<Preferences>
) : SinglePreferencesDataSource {
    companion object {
        private val SINGLE_ID_KEY = stringPreferencesKey("single_id")
    }

    override fun getSingleId(): Flow<String?> {
        return singlePreferencesDataSource.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.tag("SingleId").e(exception, "Failed to get Single id")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[SINGLE_ID_KEY]
            }
    }

    override suspend fun saveSingleId(singleId: String) {
        singlePreferencesDataSource.edit { preferences ->
            preferences[SINGLE_ID_KEY] = singleId
        }
    }

}
