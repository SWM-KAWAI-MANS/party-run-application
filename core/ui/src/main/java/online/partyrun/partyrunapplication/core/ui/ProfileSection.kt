package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunVerticalSectionBackground

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    profileContent: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        // 백그라운드 경계가 있는 배경 표현
        PartyRunVerticalSectionBackground()

        profileContent()
    }
}
