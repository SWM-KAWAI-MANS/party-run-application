package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerRecord
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.calculateSecondsElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import online.partyrun.partyrunapplication.core.network.model.util.formatEndTime
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import javax.inject.Inject

class SingleRepositoryImpl @Inject constructor() : SingleRepository {
    private val _gpsDataList = MutableStateFlow<List<GpsData>>(emptyList())
    override val gpsDataList: Flow<List<GpsData>> = _gpsDataList

    private val _totalDistance = MutableStateFlow(0)
    override val totalDistance: Flow<Int> = _totalDistance

    override fun initialize() {
        _gpsDataList.value = emptyList()
        _totalDistance.value = 0
    }

    override suspend fun addGpsData(gpsData: GpsData) {
        val updatedList = _gpsDataList.value + gpsData
        _gpsDataList.value = updatedList
    }

    override suspend fun getGpsData(): List<GpsData> {
        return _gpsDataList.value
    }

    /**
     * 싱글 UI 테스트를 위한 임시 메소드
     */
    override suspend fun getGpsDataTest(): SingleResult {
        val endTimeLocalDateType = _gpsDataList.value.lastOrNull()?.time
        val startTimeLocalDateType = _gpsDataList.value.firstOrNull()?.time

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
                records = _gpsDataList.value.mapIndexed { index, gpsData ->
                    SingleRunnerRecord(
                        altitude = gpsData.altitude,
                        latitude = gpsData.latitude,
                        longitude = gpsData.longitude,
                        time = gpsData.time,
                        distance = (index + 1) * 50.0  // 인덱스는 0부터 시작하므로, index + 1을 하여 1부터 시작하도록
                    )
                }
            ),
            startTime = startTimeLocalDateType?.let { formatTime(it) },
            targetDistance = this._totalDistance.value,
            targetDistanceFormatted = this._totalDistance.value.let {
                formatDistanceWithComma(it)
            },
            targetDistanceInKm = this._totalDistance.value.let {
                formatDistanceInKm(it)
            },
            singleDate = startTimeLocalDateType?.let { formatDate(it) } ?: ""
        )
    }

    override suspend fun setDistance(totalDistance: Int) {
        _totalDistance.emit(totalDistance)
    }
}
