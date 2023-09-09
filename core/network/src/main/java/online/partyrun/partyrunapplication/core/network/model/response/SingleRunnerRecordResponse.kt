package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerRecord
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import java.time.LocalDateTime

data class SingleRunnerRecordResponse(
    @SerializedName("altitude")
    val altitude: Double?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("distance")
    val distance: Double?
)

fun SingleRunnerRecordResponse.toDomainModel(): SingleRunnerRecord {
    val parsedTime =
        this.time?.let { LocalDateTime.parse(it, localDateTimeFormatter) } ?: LocalDateTime.MIN
    return SingleRunnerRecord(
        altitude = this.altitude ?: 0.0,
        latitude = this.latitude ?: 0.0,
        longitude = this.longitude ?: 0.0,
        time = parsedTime,
        distance = this.distance ?: 0.0
    )
}
