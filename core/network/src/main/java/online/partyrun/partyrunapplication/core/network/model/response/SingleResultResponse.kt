package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import java.time.LocalDateTime

data class SingleResultResponse(
    @SerializedName("runner")
    val singleRunnerStatus: SingleRunnerStatusResponse?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("targetDistance")
    val targetDistance: Int?
)

fun SingleResultResponse.toDomainModel(): SingleResult {
    val parsedStartTime = this.startTime?.let {
        LocalDateTime.parse(
            it,
            localDateTimeFormatter
        )
    }

    return SingleResult(
        singleRunnerStatus = this.singleRunnerStatus?.toDomainModel(parsedStartTime)
            ?: SingleRunnerStatus(),
        startTime = parsedStartTime?.let { formatTime(it) } ?: "", // "xx:xx" 형식화
        targetDistance = this.targetDistance ?: 0,
        targetDistanceFormatted = this.targetDistance?.let { formatDistanceWithComma(it) }
            ?: "", // 쉼표로 형식화
        targetDistanceInKm = this.targetDistance?.let { formatDistanceInKm(it) }
            ?: "",  // km 단위로 형식화
        singleDate = parsedStartTime?.let { formatDate(it) } ?: "" // "x월 x일" 형식화
    )
}
