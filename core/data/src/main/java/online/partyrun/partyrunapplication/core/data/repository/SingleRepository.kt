package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.running.RecordDataWithDistance
import online.partyrun.partyrunapplication.core.model.single.SingleId
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.running.GpsDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RunningTime

interface SingleRepository {

    fun initialize()
    suspend fun addGpsData(gpsData: GpsDataWithDistance)
    fun getRecordData(): Flow<RecordDataWithDistance>
    suspend fun sendRecordData(runningTime: RunningTime): Result<SingleId>

    suspend fun saveSingleId(singleId: String)

}
