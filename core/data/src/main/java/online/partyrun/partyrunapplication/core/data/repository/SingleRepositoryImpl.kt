package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.model.running.RecordDataWithDistance
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerRecord
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.model.single.SingleId
import online.partyrun.partyrunapplication.core.network.datasource.SingleDataSource
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.calculateSecondsElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import online.partyrun.partyrunapplication.core.network.model.util.formatEndTime
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.mapResultModel
import online.partyrun.partyrunapplication.core.datastore.datasource.SinglePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.running.GpsDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RunningTime
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class SingleRepositoryImpl @Inject constructor(
    private val singleDataSource: SingleDataSource,
    private val singlePreferencesDataSource: SinglePreferencesDataSource
) : SingleRepository {
    private val _recordData = MutableStateFlow(RecordDataWithDistance(emptyList()))
    override val recordData: Flow<RecordDataWithDistance> = _recordData

    override fun initialize() {
        _recordData.value = RecordDataWithDistance(emptyList())
    }

    override suspend fun addGpsData(gpsData: GpsDataWithDistance) {
        val updatedList = _recordData.value.records + gpsData
        _recordData.value = RecordDataWithDistance(updatedList)
    }

    override suspend fun getRecordData(): RecordDataWithDistance {
        return _recordData.value
    }

    override suspend fun saveSingleId(singleId: String) {
        singlePreferencesDataSource.saveSingleId(singleId)
    }

    override suspend fun getSingleId(): Flow<String?> {
        return singlePreferencesDataSource.getSingleId()
    }

    /**
     * 싱글 UI 테스트를 위한 임시 메소드
     */
    override suspend fun getGpsDataTest(): SingleResult {
        val endTimeLocalDateType = _recordData.value.records.lastOrNull()?.time
        val startTimeLocalDateType = _recordData.value.records.firstOrNull()?.time
        val distance = _recordData.value.records.lastOrNull()?.distance?.toInt() ?: 0

        return SingleResult(
            singleRunnerStatus = SingleRunnerStatus(
                endTime = formatEndTime(endTimeLocalDateType),
                elapsedTime = calculateElapsedTimeToDomainModel(
                    startTimeLocalDateType,
                    endTimeLocalDateType
                ),
                secondsElapsedTime = calculateSecondsElapsedTimeToDomainModel(
                    startTimeLocalDateType,
                    endTimeLocalDateType
                ),
                records = _recordData.value.records.mapIndexed { _, gpsData ->
                    SingleRunnerRecord(
                        altitude = gpsData.altitude,
                        latitude = gpsData.latitude,
                        longitude = gpsData.longitude,
                        time = gpsData.time,
                        distance = gpsData.distance
                    )
                }
            ),
            startTime = startTimeLocalDateType?.let { formatTime(it) },
            targetDistance = distance,
            targetDistanceFormatted = formatDistanceWithComma(distance),
            targetDistanceInKm = formatDistanceInKm(distance),
            singleDate = startTimeLocalDateType?.let { formatDate(it) } ?: ""
        )
    }

    override suspend fun sendRecordData(runningTime: RunningTime): Flow<Result<SingleId>> {
        return apiRequestFlow {
            singleDataSource.sendRecordData(
                _recordData.value.toRequestModel(
                    runningTime
                )
            )
        }.mapResultModel { it.toDomainModel() }
    }
}
