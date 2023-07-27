package online.partyrun.partyrunapplication.core.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface AgreementDataSource {
    suspend fun saveAgreementState(checked: Boolean)
    fun getAgreementState(): Flow<Boolean>
}
