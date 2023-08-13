package online.partyrun.partyrunapplication.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun UnsubscribeScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = stringResource(id = R.string.unsubscribe_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier.padding(5.dp),
            text = stringResource(id = R.string.unsubscribe_desc),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.weight(1f)) // 버튼 위 나머지 공간 채우기

        Button(
            onClick = { /* 탈퇴 로직 처리 */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(stringResource(id = R.string.unsubscribe_btn))
        }
    }
}

@Preview(showBackground = false)
@Composable
fun Preview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        UnsubscribeScreen()
    }
}