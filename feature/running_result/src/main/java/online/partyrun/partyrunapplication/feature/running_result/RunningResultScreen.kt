package online.partyrun.partyrunapplication.feature.running_result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RunningResultScreen(

) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "TEST")
    }
}

@Preview(showBackground = true)
@Composable
fun RunningResultScreenPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RunningResultScreen()
    }
}
