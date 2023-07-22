package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import online.partyrun.partyrunapplication.core.designsystem.R
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple60

@Composable
fun PartyRunMatchDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            modifier = modifier,
            cornerRadius = 16.dp,
            strokeWidth = 10.dp,
            strokeColor = Purple60,
            imageSize = 240.dp,
            imageContent = {
                Image(
                    painter = painterResource(id = R.drawable.searching_for_match),
                    contentDescription = null
                )
            },
            isResultDialog = false
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PartyRunResultDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties( // 다이얼로그 width 제한 해제
                usePlatformDefaultWidth = false
            )
    ) {
        DialogContent(
            modifier = modifier,
            cornerRadius = 25.dp,
            strokeWidth = 0.dp,
            strokeColor = Color.Transparent,
            imageSize = 150.dp,
            imageContent = {
                Image(
                    painter = painterResource(id = R.drawable.match_result),
                    contentDescription = null
                )
            },
            isResultDialog = true
        ) {
            content()
        }
    }
}

@Composable
fun PartyRunCancelDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            modifier = modifier,
            cornerRadius = 25.dp,
            strokeWidth = 10.dp,
            strokeColor = Purple60,
            imageSize = 150.dp,
            imageContent = {},
            isResultDialog = false
        ) {
            content()
        }
    }
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    cornerRadius: Dp,
    strokeWidth: Dp,
    strokeColor: Color,
    imageSize: Dp,
    isResultDialog: Boolean,
    imageContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = if (isResultDialog) Modifier else Modifier
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(cornerRadius),
                color = if (isResultDialog) Color.Transparent else Color.White,
                border = BorderStroke(strokeWidth, strokeColor)
            ) {
                content()
            }
        }

        if (isResultDialog) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(75.dp)
            ) {
                imageContent()
            }
        } else {
            Column(
                modifier = Modifier
                    .size(imageSize)
                    .align(Alignment.TopCenter)
            ) {
                imageContent()
            }
        }
    }
}
