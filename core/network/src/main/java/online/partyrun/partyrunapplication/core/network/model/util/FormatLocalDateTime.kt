package online.partyrun.partyrunapplication.core.network.model.util

import java.text.NumberFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * LocalDateTime 객체를 받아서 시가 0인지 아닌지를 확인하고,
 * 시가 0이라면 -> 분:초 단위 String으로 변환
 * 시가 0이 아니라면 시가 존재한다는 것이므로 -> 시:분:초 단위 String으로 변환
 */
fun formatTime(time: LocalDateTime): String {
    return if (time.hour != 0) {
        time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    } else {
        time.format(DateTimeFormatter.ofPattern("mm:ss"))
    }
}

/**
 * LocalDateTime의 차이를 계산하여 Duration을 반환하고, 이를 문자열로 변환
 * 달리기를 수행한 경과 시간 반환
 * 마찬가지로 시가 존재하면 분:초 반환, 그렇지 않다면 시:분:초 반환
 */
fun calculateElapsedTime(startTime: LocalDateTime, endTime: LocalDateTime): String {
    val duration = Duration.between(startTime, endTime)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun calculateSecondsElapsedTime(startTime: LocalDateTime, endTime: LocalDateTime): Long {
    return Duration.between(startTime, endTime).toSeconds()
}

fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("M월 d일")
    return dateTime.format(formatter)
}

fun formatDistanceWithComma(distance: Int): String {
    return NumberFormat.getNumberInstance().format(distance).plus("m")
}

fun formatDistanceInKm(distance: Int): String {
    return (distance / 1000).toString().plus("km")
}
