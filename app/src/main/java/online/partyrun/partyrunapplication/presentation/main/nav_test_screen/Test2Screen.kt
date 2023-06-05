package online.partyrun.partyrunapplication.presentation.main.nav_test_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Test2Screen(
    navigateToTest3: () -> Unit,
    userName: String? = ""
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, $userName",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick ={
                    navigateToTest3()
                }
            ) {
                Text(text = "Set up your Profile")
            }
        }
    }
}
