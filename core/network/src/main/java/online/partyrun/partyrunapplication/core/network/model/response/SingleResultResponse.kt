package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import java.time.LocalDateTime

data class SingleResultResponse(
    @SerializedName("runningTime")
    val runningTime: RunningTimeResponse,
    @SerializedName("records")
    val records: List<SingleRunnerRecordResponse>,
)

fun SingleResultResponse.toDomainModel(): SingleResult {
    val parsedStartTime = records.firstOrNull()?.time?.let {
        LocalDateTime.parse(
            it,
            localDateTimeFormatter
        )
    }

    val targetDistance = records.lastOrNull()?.distance?.toInt() ?: 0

    return SingleResult(
        singleRunnerStatus = SingleRunnerStatus(
            records = this.records.map { it.toDomainModel() } // 각 record를 도메인 모델로 변환
        ),
        runningTime = runningTime.toDomainModel(),
        targetDistance = targetDistance,
        targetDistanceFormatted = formatDistanceWithComma(targetDistance),
        targetDistanceInKm = formatDistanceInKm(targetDistance),
        singleDate = parsedStartTime?.let {
            formatDate(it)
        }.orEmpty() // "x월 x일" format
    )
}
