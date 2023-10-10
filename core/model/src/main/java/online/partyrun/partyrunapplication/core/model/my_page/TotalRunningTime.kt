package online.partyrun.partyrunapplication.core.model.my_page

data class TotalRunningTime(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)

fun TotalRunningTime.toElapsedTimeString(): String {
    return String.format("%02d:%02d", hours, minutes)
}

