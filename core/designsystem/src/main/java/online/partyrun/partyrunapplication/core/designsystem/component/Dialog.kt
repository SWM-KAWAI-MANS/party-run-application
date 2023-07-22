package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
            }
        ) {
            content()
        }
    }
}

@Composable
fun PartyRunResultDialog(
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
            strokeWidth = 0.dp,
            strokeColor = Color.Transparent,
            imageSize = 150.dp,
            imageContent = {
                Image(
                    painter = painterResource(id = R.drawable.match_result),
                    contentDescription = null
                )
            }
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
            imageContent = {}
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
    imageContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(cornerRadius),
                color = Color.White,
                border = BorderStroke(strokeWidth, strokeColor)
            ) {
                content()
            }
        }
        Column(
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.TopCenter)
        ) {
            imageContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartyRunCancelDialogPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PartyRunCancelDialog(
            onDismissRequest = {},
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "TEST",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}
