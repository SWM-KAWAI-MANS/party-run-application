package online.partyrun.partyrunapplication.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Android 애플리케이션에 대한 색조 테마(TintTheme)를 설정하는 클래스와 Composition local 정의
 */
@Immutable
data class TintTheme(
    val iconTint: Color? = null,
)

/**
 * A composition local for [TintTheme].
 * TintTheme에 대한 Composition local
 * Composition local은 계층 구조의 위에서 아래로 값을 전달하는 메커니즘으로, 상위 Composable에서 하위 Composable까지 특정 값을 "주입"하는 데 사용
 */
val LocalTintTheme = staticCompositionLocalOf { TintTheme() }
