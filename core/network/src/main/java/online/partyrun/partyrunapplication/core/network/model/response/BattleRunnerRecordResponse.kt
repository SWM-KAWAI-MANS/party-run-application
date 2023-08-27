package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.BattleRunnerRecord
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import java.time.LocalDateTime

data class BattleRunnerRecordResponse(
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

fun BattleRunnerRecordResponse.toDomainModel(): BattleRunnerRecord {
    val parsedTime =
        this.time?.let { LocalDateTime.parse(it, localDateTimeFormatter) } ?: LocalDateTime.MIN
    return BattleRunnerRecord(
        altitude = this.altitude ?: 0.0,
        latitude = this.latitude ?: 0.0,
        longitude = this.longitude ?: 0.0,
        time = parsedTime,
        distance = this.distance ?: 0.0
    )
}
