package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
        Box(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Surface(
                    modifier = modifier,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(10.dp, Purple60)
                ) {
                    content()
                }
            }
            Column(
                modifier = Modifier
                    .size(240.dp).align(Alignment.TopCenter)
            ) {
                Image(painter = painterResource(id = R.drawable.searching_for_match), contentDescription = null)
            }
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
        Box(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Surface(
                    modifier = modifier,
                    shape = RoundedCornerShape(25.dp),
                    color = Color.White,
                ) {
                    content()
                }
            }
            Column(
                modifier = Modifier
                    .size(150.dp).align(Alignment.TopCenter)
            ) {
                Image(painter = painterResource(id = R.drawable.match_result), contentDescription = null)
            }
        }
    }
}