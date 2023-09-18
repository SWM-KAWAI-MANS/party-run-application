package online.partyrun.partyrunapplication.core.model.running

data class RunningTime(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
) {
    companion object {
        fun fromSeconds(totalSeconds: Int): RunningTime {
            val hours = totalSeconds / 3600
            val remainingSeconds = totalSeconds % 3600
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return RunningTime(hours, minutes, seconds)
        }
    }
}

fun RunningTime.toElapsedTimeString(): String {
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun RunningTime.toElapsedSeconds(): Int {
    return hours * 3600 + minutes * 60 + seconds
}