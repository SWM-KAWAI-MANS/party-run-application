package online.partyrun.partyrunapplication.feature.running.battle.finish

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun FinishScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 뒤에 대결 현황은 그대로 두되 종료 스크린만을 위에 띄워주기 위한 반투명 배경셋팅
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        // 뒷배경 blur 형광 원형 이미지
        Box(
            modifier = Modifier
                .widthIn(max = max(350.dp, with(LocalDensity.current) { 350.sp.toDp() }))
                .heightIn(max = max(350.dp, with(LocalDensity.current) { 350.sp.toDp() }))
        ) {
            Image(
                painter = painterResource(R.drawable.background_finish_blur),
                contentDescription = stringResource(id = R.string.background_finish_blur),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp
                )
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PartyRunGradientText(
                modifier = Modifier
                    .padding(
                        vertical = 40.dp
                    )
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.finish),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(id = R.string.finish_comment),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        // Lottie 애니메이션 이펙트
        LottieImage(modifier = Modifier, rawAnimation = R.raw.congratulations)
    }
}

@Preview(showBackground = true)
@Composable
fun FinishScreenPreview() {
    FinishScreen()
}
