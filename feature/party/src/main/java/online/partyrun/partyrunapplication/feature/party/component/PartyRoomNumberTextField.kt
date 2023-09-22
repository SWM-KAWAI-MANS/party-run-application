package online.partyrun.partyrunapplication.feature.party.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyDialogTextFieldColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PartyRoomNumberTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController? = null
) {
    val maxTextLength = 6

    BasicTextField(
        value = text,
        onValueChange = { newText ->
            if (isValidNumberInput(newText, maxTextLength)) {
                onTextChanged(newText)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number // 숫자만 보여주도록 제한
        ),
        visualTransformation = VisualTransformation.None,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                text.forEachIndexed { _, char ->
                    PartyNumberCharContainer(char)
                }
                repeat(maxTextLength - text.length) {
                    PartyNumberCharContainer(' ')
                }
            }
        },
    )
}

@Composable
private fun PartyNumberCharContainer(
    text: Char,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 32.dp, height = 40.dp)
            .background(color = PartyDialogTextFieldColor, RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private fun isValidNumberInput(input: String, maxLength: Int): Boolean {
    return input.all { it.isDigit() } && input.length <= maxLength
}
