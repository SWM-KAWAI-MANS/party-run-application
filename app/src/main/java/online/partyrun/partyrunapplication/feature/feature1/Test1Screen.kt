package online.partyrun.partyrunapplication.feature.feature1

import androidx.compose.runtime.Composable
import online.partyrun.partyrunapplication.feature.test.TestScreen

@Composable
fun Test1Screen(
    navigateToTest2WithArgs: (String) -> Unit
) {
    TestScreen()
}
