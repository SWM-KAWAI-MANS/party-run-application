package online.partyrun.partyrunapplication.core.model.util

import java.time.Duration

fun formatPace(pace: Double): String {
    val minutesPart = (pace / 60).toInt()
    val secondsPart = (pace % 60).toInt()

    // %02d는 정수를 두 자리로 표현하는데, 만약 한 자리수면 앞에 0 추가
    return "${minutesPart}'${String.format("%02d", secondsPart)}''"
}

fun formatDurationToTimeString(duration: Duration): String {
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
