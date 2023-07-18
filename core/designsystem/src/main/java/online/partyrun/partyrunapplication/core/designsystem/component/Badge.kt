package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.R

@Composable
fun PartyRunBadge(
    modifier: Modifier = Modifier,
    image: Painter,
    contentAlignment: Alignment = Alignment.Center
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Image(painter = image, contentDescription = null)
    }
}

@Composable
fun LevelBadge(
    level: Int,
    modifier: Modifier = Modifier,
) {
    val levelImageRes = when (level) {
        1 -> R.drawable.level1
        2 -> R.drawable.level2
        3 -> R.drawable.level3
        4 -> R.drawable.level4
        5 -> R.drawable.level5
        6 -> R.drawable.level6
        7 -> R.drawable.level7
        else -> R.drawable.level1 // 디폴트 레벨 이미지
    }

    Box(
        modifier = modifier.width(81.dp).height(25.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = levelImageRes), contentDescription = null)
    }
}
