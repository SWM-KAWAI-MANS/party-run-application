package online.partyrun.partyrunapplication.feature.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedButton
import online.partyrun.partyrunapplication.core.ui.SettingsTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsubscribeScreen(
    onSignOut: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

    if (settingsUiState.isAccountDeletionSuccess) {
        val message = stringResource(id = R.string.unsubscribe_success_message)
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
        onSignOut() // 파이어베이스 구글 로그아웃 -> 스플래시로 되돌아감.
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsTopAppBar {
                navigateBack()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // 회원 탈퇴에 관한 설명
                DescriptionTexts()

                Spacer(modifier = Modifier.size(10.dp))

                // 탈퇴에 대한 이유 선택
                ReasonSpinner()

                Spacer(modifier = Modifier.weight(1f)) // 버튼 위 나머지 공간 채우기

                // 확인, 취소
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CancelButton(navigateBack = navigateBack)
                    ConfirmButton(
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun DescriptionTexts() {
    Text(
        modifier = Modifier.padding(5.dp),
        text = stringResource(id = R.string.unsubscribe_title_1),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Text(
        modifier = Modifier.padding(5.dp),
        text = stringResource(id = R.string.unsubscribe_desc_1),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.size(30.dp))
    Text(
        modifier = Modifier.padding(5.dp),
        text = stringResource(id = R.string.unsubscribe_title_2),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Text(
        modifier = Modifier.padding(5.dp),
        text = stringResource(id = R.string.unsubscribe_desc_2),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
private fun CancelButton(navigateBack: () -> Unit) {
    PartyRunOutlinedButton(
        onClick = { navigateBack() },
        shape = RoundedCornerShape(35.dp),
        borderStrokeWidth = 5.dp,
        modifier = Modifier
            .shadow(5.dp, shape = CircleShape),
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = stringResource(id = R.string.unsubscribe_cancel_btn),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun ConfirmButton(
    settingsViewModel: SettingsViewModel
) {
    PartyRunGradientButton(
        onClick = {
            settingsViewModel.deleteAccount()
        },
        modifier = Modifier
            .shadow(5.dp, shape = CircleShape)
    ) {
        Text(
            text = stringResource(id = R.string.unsubscribe_confirm_btn),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ReasonSpinner() {
    var expanded by remember { mutableStateOf(false) }
    val unsubscribeReasons = listOf(
        stringResource(id = R.string.unsubscribe_reason_1),
        stringResource(id = R.string.unsubscribe_reason_2),
        stringResource(id = R.string.unsubscribe_reason_3),
        stringResource(id = R.string.unsubscribe_reason_4),
        stringResource(id = R.string.unsubscribe_reason_5),
    )
    var selectedReason by remember { mutableStateOf(unsubscribeReasons.first()) }

    Box(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(15.dp),
                text = selectedReason
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(id = R.string.dropdown_desc)
            )
        }

        DropdownMenu(
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            unsubscribeReasons.forEach { reason ->
                DropdownMenuItem(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.surface), // 경계 추가
                    onClick = {
                        selectedReason = reason
                        expanded = false
                    },
                    text = {
                        Text(text = reason)
                    }
                )
            }
        }
    }
}
