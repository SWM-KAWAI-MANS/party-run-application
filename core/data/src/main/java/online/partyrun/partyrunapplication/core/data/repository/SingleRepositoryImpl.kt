package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import online.partyrun.partyrunapplication.core.model.running.RecordDataWithDistance
import online.partyrun.partyrunapplication.core.model.single.SingleId
import online.partyrun.partyrunapplication.core.network.datasource.SingleDataSource
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.toResultModel
import online.partyrun.partyrunapplication.core.datastore.datasource.SinglePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.running.GpsDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RunningTime
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class SingleRepositoryImpl @Inject constructor(
    private val singleDataSource: SingleDataSource,
    private val singlePreferencesDataSource: SinglePreferencesDataSource,
) : SingleRepository {
    private val _recordData = MutableStateFlow(RecordDataWithDistance(emptyList()))
    private val recordData: Flow<RecordDataWithDistance> = _recordData

    override fun initialize() {
        _recordData.value = RecordDataWithDistance(emptyList())
    }

    override suspend fun addGpsData(gpsData: GpsDataWithDistance) {
        val updatedList = _recordData.value.records + gpsData
        _recordData.value = RecordDataWithDistance(updatedList)
    }

    override fun getRecordData(): Flow<RecordDataWithDistance> {
        return recordData
    }

    override suspend fun saveSingleId(singleId: String) {
        singlePreferencesDataSource.saveSingleId(singleId)
    }

    override suspend fun sendRecordData(runningTime: RunningTime): Result<SingleId> {
        return singleDataSource
            .sendRecordData(_recordData.value.toRequestModel(runningTime))
            .toResultModel { it.toDomainModel() }
    }
}
