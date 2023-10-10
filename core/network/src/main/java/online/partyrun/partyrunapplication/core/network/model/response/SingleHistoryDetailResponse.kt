package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.my_page.RunningHistoryDetail
import online.partyrun.partyrunapplication.core.model.running.RunningTime
import online.partyrun.partyrunapplication.core.model.running.toElapsedTimeString
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import java.time.LocalDateTime

data class SingleHistoryDetailResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("runningTime")
    val runningTime: RunningTime?,
    @SerializedName("distance")
    val distance: Double?
)

fun SingleHistoryDetailResponse.toDomainModel(): RunningHistoryDetail {
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
        DistanceFormatted = formatDistanceWithComma(this.distance?.toInt() ?: 0)
    )
}
