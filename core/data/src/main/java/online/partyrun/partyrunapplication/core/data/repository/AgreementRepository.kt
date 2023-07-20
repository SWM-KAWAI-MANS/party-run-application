package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AgreementRepository {

    suspend fun saveAgreementState(checked: Boolean)
    fun getAgreementState(): Flow<Boolean>
}
