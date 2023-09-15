package online.partyrun.partyrunapplication.core.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface SinglePreferencesDataSource {
    fun getSingleId(): Flow<String?>
    suspend fun saveSingleId(singleId: String)
}
