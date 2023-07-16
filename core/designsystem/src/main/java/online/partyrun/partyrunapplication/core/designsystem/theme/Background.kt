package online.partyrun.partyrunapplication.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable // 불변성 데이터 클래스 선언 -> 한 번 생성된 후에 내부 상태 변경 불가
data class BackgroundTheme(
    val color: Color = Color.Unspecified, // 배경색
    val tonalElevation: Dp = Dp.Unspecified, // 톤 높이(즉, '음영' 또는 '입체감'을 나타내는 값)
)

/**
 * LocalBackgroundTheme를 사용하여 현재 컴포지션에서 BackgroundTheme을 얻거나 설정 가능
 */
val LocalBackgroundTheme =
    staticCompositionLocalOf { BackgroundTheme() }
