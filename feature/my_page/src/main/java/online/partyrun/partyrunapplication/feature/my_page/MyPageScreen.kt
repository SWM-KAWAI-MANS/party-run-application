package online.partyrun.partyrunapplication.feature.my_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "마이 페이지"
        )
        Button(
            onClick = {
                viewModel.signOutFromGoogle()
                onSignOut()
            }
        ) {
            Text(text = "Sign out")
        }
    }
}
