package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.my_page.RunningHistoryDetail
import online.partyrun.partyrunapplication.core.model.running.RunningTime
import online.partyrun.partyrunapplication.core.model.running.toElapsedTimeString
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import java.time.LocalDateTime

data class RunningHistoryDetailResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("runningTime")
    val runningTime: RunningTime?,
    @SerializedName("distance")
    val distance: Double?
)

fun RunningHistoryDetailResponse.toDomainModel(): RunningHistoryDetail {
    val parsedDate = startTime?.let {
        LocalDateTime.parse(
            it,
            DateTimeUtils.localDateTimeFormatter
        )
    }

    return RunningHistoryDetail(
        id = this.id ?: "",
        date = parsedDate?.let { formatDate(it) } ?: "",
        runningTime = this.runningTime?.toElapsedTimeString() ?: "00:00:00",
        distanceFormatted = formatDistanceWithComma(this.distance?.toInt() ?: 0)
    )
}
