package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.theme.DarkNavy30
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple90

/**
 * @param onClick 사용자 클릭 시 호출
 * @param modifier 버튼에 적용할 Modifier
 * @param enabled 버튼의 활성화 상태를 나타
 * @param contentPadding 컨테이너와 컨테이너 사이에 내부적으로 적용할 padding 값
 * @param content 버튼 내용
 */
@Composable
fun PartyRunOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    enabledColor: Color = MaterialTheme.colorScheme.outline,
    disabledColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
        ),
        border = BorderStroke(
            width = 10.dp,
            color = if (enabled) enabledColor else disabledColor,
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun PartyRunOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    PartyRunOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        },
    ) {
        PartyRunButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

/**
 * @param onClick 사용자 클릭 시 호출
 * @param modifier 버튼에 적용할 Modifier
 * @param enabled 버튼의 활성화 상태를 나타
 * @param contentPadding 컨테이너와 컨테이너 사이에 내부적으로 적용할 padding 값
 * @param content 버튼 내용
 */
@Composable
fun PartyRunTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    PartyRunTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        PartyRunButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun PartyRunTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.onBackground, // 배경 색 변경
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
        ),
        content = content,
    )
}

@Composable
fun PartyRunMatchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = Purple90,
    containerColor: Color = DarkNavy30, // 배경 색
    content: @Composable RowScope.() -> Unit,
) {
    PartyRunTextButton(
        onClick = onClick,
        modifier = modifier.width(187.dp).height(60.dp),
        enabled = enabled,
        contentColor = contentColor,
        containerColor = containerColor,
        content = content
    )
}

@Composable
fun PartyRunAnimatedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = visible
        ) {
            Button(
                onClick = onClick
            ) {
                content()
            }
        }
    }
}

@Composable
private fun PartyRunButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier
            .padding(
                start = if (leadingIcon != null) {
                    ButtonDefaults.IconSpacing
                } else {
                    0.dp
                },
            ),
    ) {
        text()
    }
}
