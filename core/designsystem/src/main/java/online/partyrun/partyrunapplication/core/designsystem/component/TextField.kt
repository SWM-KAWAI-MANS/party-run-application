package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons

enum class TextInputType {
    Email, PASSWORD, FIELD
}

@ExperimentalComposeUiApi
@Composable
fun PartyRunTextInput(
    modifier: Modifier = Modifier,
    type: TextInputType,
    placeholder: @Composable () -> Unit,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    keyboardController: SoftwareKeyboardController? = null,
    focusManager: FocusManager,
    focusRequester: FocusRequester = FocusRequester(),
    onValueChangeListener: (String) -> Unit = {},
) {
    when (type) {
        TextInputType.Email -> {
            TextInput(
                inputType = InputType.Email,
                modifier = modifier,
                leadingIcon = leadingIconType(InputType.Email),
                placeholder = placeholder,
                maxLines = maxLines,
                singleLine = singleLine,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusRequester.requestFocus()
                    }
                ),
                onValueChangeListener = onValueChangeListener
            )
        }

        TextInputType.PASSWORD -> {
            TextInput(
                inputType = InputType.Password,
                modifier = modifier,
                leadingIcon = leadingIconType(InputType.Password),
                placeholder = placeholder,
                maxLines = maxLines,
                singleLine = singleLine,
                focusRequester = focusRequester,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                onValueChangeListener = onValueChangeListener
            )
        }

        TextInputType.FIELD -> {
            TextInput(
                inputType = InputType.FIELD,
                placeholder = placeholder,
                maxLines = maxLines,
                singleLine = singleLine,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                onValueChangeListener = onValueChangeListener
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    inputType: InputType,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable () -> Unit,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChangeListener: (value: String) -> Unit = {},
) {
    var textValue by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChangeListener(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester ?: FocusRequester()),
        leadingIcon = leadingIcon,
        placeholder = placeholder,
        label = {
            if (inputType != InputType.FIELD) {
                Text(text = inputType.label, color = Color.Black)
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.onBackground,
            placeholderColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.background,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = PartyRunIcons.CancelFilled),
                contentDescription = null,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures {
                        textValue = ""
                    }
                }
            )
        },
        maxLines = maxLines,
        singleLine = singleLine,
        shape = RoundedCornerShape(15.dp),
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions,
        enabled = true // true로 고정. false 필요한 상황이 생길 시 파라미터로 전달
    )
}

sealed class InputType(
    val label: String = "",
    val icon: ImageVector? = null,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Email : InputType(
        label = "이메일",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "비밀번호",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = PasswordVisualTransformation()
    )

    object FIELD : InputType(
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        visualTransformation = VisualTransformation.None
    )
}

@ExperimentalComposeUiApi
fun Modifier.addFocusCleaner(
    keyboardController: SoftwareKeyboardController,
    focusManager: FocusManager,
    doOnClear: () -> Unit = {}
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
            keyboardController.hide()
        })
    }
}

fun leadingIconType(inputType: InputType) =
    @Composable {
        if (inputType != InputType.FIELD) {
            Icon(
                imageVector = inputType.icon!!,
                contentDescription = null
            )
        }
    }

