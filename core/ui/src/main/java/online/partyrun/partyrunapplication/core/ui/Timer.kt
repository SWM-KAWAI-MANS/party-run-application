package online.partyrun.partyrunapplication.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun FormatElapsedTimer() {
    val elapsedTime = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = elapsedTime) {
        while (true) {
            delay(1000L) // 1초 대기
            elapsedTime.value++ // elapsedTime 증가
        }
    }

    val hours = elapsedTime.value / 3600
    val minutes = (elapsedTime.value % 3600) / 60
    val seconds = elapsedTime.value % 60

    val displayTime = formatTime(hours, minutes, seconds)
    Text(
        text = displayTime,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun FormatRemainingTimer(totalTime: Int) {
    val remainingTime = remember { mutableStateOf(totalTime) }

    LaunchedEffect(key1 = remainingTime) {
        while (remainingTime.value > 0) {
            delay(1000L) // 1초 대기
            remainingTime.value-- // remainingTime 감소
        }
    }

    val hours = remainingTime.value / 3600
    val minutes = (remainingTime.value % 3600) / 60
    val seconds = remainingTime.value % 60

    val displayTime = formatTime(hours, minutes, seconds)
    Text(
        text = displayTime,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

fun formatTime(hours: Int, minutes: Int, seconds: Int): String {
    if (hours > 0) {
        return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
    }
    return "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
}
