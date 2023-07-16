package online.partyrun.partyrunapplication.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * 라이트 모드의 PartyRun Project 컬러 스킴 정의
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = Purple60, // 앱의 주요 부분에서 사용되며, 앱의 브랜드 색상
    onPrimary = White10, // 주요 색상 위에 그려지는 콘텐츠(텍스트, 아이콘 등)의 색상
    primaryContainer = Purple30, // 주요 색상을 사용하는 컨테이너의 배경 색상 // Text 컴포넌트를 포함하는 Box 컴포넌트는 "컨테이너"
    onPrimaryContainer = Purple20, // primaryContainer 색상 위에 그려지는 콘텐츠의 색상
    secondary = DarkNavy10, // 보조 색상
    onSecondary = Gray90, // 보조 색상 위에 그려지는 콘텐츠의 색상
    secondaryContainer = DarkNavy40,
    error = Red80, // 오류 메시지나 '실패' 상태를 나타내는데 사용
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkNavy10, // 앱의 배경 색상
    onBackground = DarkNavy60, // 배경색상 위에 그려지는 콘텐츠의 색상
    surface = DarkNavy10, // UI 요소(카드, 시트 등)의 배경 색상
    onSurface = DarkNavy60, // surface 위에 그려지는 콘텐츠의 색상
    surfaceVariant = Purple20,
    onSurfaceVariant = Purple80,
    inverseSurface = DarkNavy70, // Surface 색상의 반대색
    inverseOnSurface = DarkNavy70, // onSurface 색상의 반대색
    outline = Purple10, // 외곽선 색상
)

/**
 * 다크 모드의 PartyRun Project 컬러 스킴 정의
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple60, // 앱의 주요 부분에서 사용되며, 앱의 브랜드 색상
    onPrimary = White10, // 주요 색상 위에 그려지는 콘텐츠(텍스트, 아이콘 등)의 색상
    primaryContainer = Purple30, // 주요 색상을 사용하는 컨테이너의 배경 색상 // Text 컴포넌트를 포함하는 Box 컴포넌트는 "컨테이너"
    onPrimaryContainer = Purple20, // primaryContainer 색상 위에 그려지는 콘텐츠의 색상
    secondary = DarkNavy10, // 보조 색상
    onSecondary = Gray90, // 보조 색상 위에 그려지는 콘텐츠의 색상
    secondaryContainer = DarkNavy40,
    error = Red80, // 오류 메시지나 '실패' 상태를 나타내는데 사용
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkNavy10, // 앱의 배경 색상
    onBackground = DarkNavy60, // 배경색상 위에 그려지는 콘텐츠의 색상
    surface = DarkNavy10, // UI 요소(카드, 시트 등)의 배경 색상
    onSurface = DarkNavy60, // surface 위에 그려지는 콘텐츠의 색상
    surfaceVariant = Purple20,
    onSurfaceVariant = Purple80,
    inverseSurface = DarkNavy70, // Surface 색상의 반대색
    inverseOnSurface = DarkNavy70, // onSurface 색상의 반대색
    outline = Purple10, // 외곽선 색상
)

/**
 * 라이트 모드의 안드로이드 컬러 스킴 정의
 */
@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    inverseSurface = DarkGreenGray20,
    inverseOnSurface = DarkGreenGray95,
    outline = GreenGray50,
)

/**
 * 다크 모드의 안드로이드 컬러 스킴 정의
 */
@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60,
)

/**
 * 라이트 모드에 대한 그라디언트 컬러 설정
 */
val LightAndroidGradientColors =
    GradientColors(top = Purple20, bottom = Purple10, container = Purple30)

/**
 * 다크 모드에 대한 그라디언트 컬러 설정
 */
val DarkAndroidGradientColors =
    GradientColors(top = Purple20, bottom = Purple10, container = Purple30)

/**
 * 라이트 모드에 대한 배경 테마
 */
val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * 다크 모드에 대한 배경 테마
 */
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)

/**
 * Project Main theme.
 * 앱의 전체 테마 설정
 * 3가지 매개변수를 받는다. darkTheme, androidTheme, disableDynamicTheming.
 * 위에서 정의된 컬러 스킴, 그라디언트 컬러, 배경 테마를 선택적으로 적용, 컴포지션 로컬에 이러한 테마를 제공
 * 이 함수를 사용하는 모든 하위 컴포넌트들은 이 컴포지션 로컬을 통해 현재의 테마 설정에 접근 가능
 *
 * @param darkTheme 테마가 어두운 색 구성표를 사용할지 여부 => 기본적으로 사용자의 시스템 설정을 따름
 * @param androidTheme 앱의 테마가 기본 테마 대신 Android 테마 색상표를 사용해야 하는지 여부
 * @param disableDynamicTheming 동적 테마 사용을 비활성화 할지를 결정. 동적 테마를 지원하는 경우, 앱은 사용자의 시스템 설정에 따라 테마의 색상을 동적으로 변경
 */
@Composable
fun PartyRunApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit, // 이 함수에 의해 제공되는 테마 설정을 사용하여 UI를 구성하는 데 사용
) {
    // Color scheme
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    val emptyGradientColors =
        GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp)) // 컨테이너의 색상을 "surface color"과 관련된 2dp 높이에서의 색상으로 설정하며, 상단과 하단의 색상은 지정하지 않았음
    val defaultGradientColors = GradientColors(
        // 사용자의 테마 설정에 따라 그라데이션 색 결정
        top = Purple70,
        bottom = Purple10,
        container = Purple30,
    )
    val gradientColors = when {
        androidTheme -> if (darkTheme) DarkAndroidGradientColors else LightAndroidGradientColors
        !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }
    // Background theme
    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )
    val backgroundTheme = when {
        androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
        else -> defaultBackgroundTheme
    }
    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }
    // CompositionLocalProvider를 사용하여 이 함수에서 생성된 테마 설정을 하위 @Composable 함수에 제공
    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        // 이렇게 하면 content 함수 내부에서 LocalGradientColors, LocalBackgroundTheme, 그리고 LocalTintTheme에 접근 가능
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PartyRunTypography,
            content = content,
        )
    }
}

// supportsDynamicTheming() 함수는 애플리케이션이 동적 테마를 지원하는지 여부를 결정하는 함수
// Android S 이상의 버전에서만 동적 테마가 지원되므로, 이 함수는 현재 실행 중인 Android 버전이 Android S 이상인지 확인
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
