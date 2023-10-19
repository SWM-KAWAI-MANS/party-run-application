package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.database.model.BattleRunningHistoryEntity
import online.partyrun.partyrunapplication.core.database.model.SingleRunningHistoryEntity
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

fun RunningHistoryDetailResponse.getFormattedRunningTime(): String {
    return this.runningTime?.toElapsedTimeString() ?: "00:00:00"
}

fun RunningHistoryDetailResponse.getParsedDate(): LocalDateTime? {
    return startTime?.let {
        LocalDateTime.parse(
            it,
            DateTimeUtils.localDateTimeFormatter
        )
    }
}

fun RunningHistoryDetailResponse.getFormattedDate(): String {
    return getParsedDate()?.let { formatDate(it) } ?: ""
}

fun RunningHistoryDetailResponse.toDomainModel(): RunningHistoryDetail {
    return RunningHistoryDetail(
        id = this.id ?: "",
        date = getFormattedDate(),
        runningTime = getFormattedRunningTime(),
        distanceFormatted = formatDistanceWithComma(this.distance?.toInt() ?: 0)
    )
}

fun RunningHistoryDetailResponse.toSingleRunningHistoryEntity(): SingleRunningHistoryEntity {
    return SingleRunningHistoryEntity(
        id = this.id ?: "",
        date = getFormattedDate(),
        runningTime = getFormattedRunningTime(),
        distanceFormatted = formatDistanceWithComma(this.distance?.toInt() ?: 0)
    )
}

fun RunningHistoryDetailResponse.toBattleRunningHistoryEntity(): BattleRunningHistoryEntity {
    return BattleRunningHistoryEntity(
        id = this.id ?: "",
        date = getFormattedDate(),
        runningTime = getFormattedRunningTime(),
        distanceFormatted = formatDistanceWithComma(this.distance?.toInt() ?: 0)
    )
}
