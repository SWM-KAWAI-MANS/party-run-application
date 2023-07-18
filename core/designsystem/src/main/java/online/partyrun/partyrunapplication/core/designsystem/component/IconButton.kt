package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple40
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple60
import online.partyrun.partyrunapplication.core.designsystem.theme.White10

/**
 * @param checked 토글 버튼이 현재 선택되어 있는지 여부 확인
 * @param onCheckedChange 사용자가 토글 버튼을 클릭하고 선택한 토글을 전환할 때 호출
 * @param modifier 토글 버튼에 적용할 Modifier.
 * @param enabled 토글 버튼의 활성화 상태 제어
 * @param icon 선택하지 않은 경우 표시할 아이콘의 컨텐츠
 * @param checkedIcon 선택 시 표시되는 아이콘의 컨텐츠
 */
@Composable
fun PartyRunIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    checkedContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledContainerColor: Color = MaterialTheme.colorScheme.onBackground,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = checkedContainerColor,
            checkedContentColor = checkedContentColor,
            disabledContainerColor = if (checked) {
                disabledContainerColor
            } else {
                Color.Transparent
            },
        ),
    ) {
        if (checked) checkedIcon() else icon()
    }
}

@Composable
fun PartyRunCircularIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Purple60,
    contentColor: Color = White10,
    disabledContainerColor: Color = MaterialTheme.colorScheme.onBackground,
    disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = Purple40,
    borderWidth: Dp = 3.dp,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(56.dp) // 원형 크기 설정
            .background(
                color = containerColor,
                shape = CircleShape // 원형 모양으로 설정
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = CircleShape // 테두리 모양도 원형으로 설정
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            colors = IconButtonDefaults.filledIconButtonColors(
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor,
            )
        ) {
            icon()
        }
    }
}
