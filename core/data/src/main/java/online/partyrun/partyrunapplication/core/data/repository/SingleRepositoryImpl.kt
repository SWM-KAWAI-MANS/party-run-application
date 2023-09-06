package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import online.partyrun.partyrunapplication.core.model.running.GpsData
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

    override suspend fun setDistance(totalDistance: Int) {
        _totalDistance.emit(totalDistance)
    }
}
