package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.BottomHalfOvalGradientShape
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunMatchButton

@Composable
fun MatchStartButtonLayout(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        BottomHalfOvalGradientShape(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            PartyRunMatchButton(
                onClick = onClick,
            ) {
                content()
            }
        }
    }
}
