package online.partyrun.partyrunapplication.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A class to model gradient color values for Party Run Project.
 *
 * @param top  // Gradient의 상단 색상
 * @param bottom // Gradient의 하단 색상
 * @param container // Gradient가 적용되는 컨테이너 색상
 */
@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

/**
 * A composition local for [GradientColors].
 */
val LocalGradientColors = // LocalGradientColors라는 Composition Local 선언 -> Compose의 상태 공유 메커니즘
    staticCompositionLocalOf {
        // 기본적으로 GradientColors 인스턴스를 제공하는 Composition Local 생성
        GradientColors()
    }
