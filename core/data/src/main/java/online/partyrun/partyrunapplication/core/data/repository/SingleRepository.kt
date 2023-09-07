package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.running.GpsData

interface SingleRepository {
    val gpsDataList: Flow<List<GpsData>>
    val totalDistance: Flow<Int>

    fun initialize()
    suspend fun addGpsData(gpsData: GpsData)
    suspend fun getGpsData(): List<GpsData>
    suspend fun setDistance(totalDistance: Int)
}