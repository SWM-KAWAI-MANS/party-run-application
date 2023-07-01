package online.partyrun.partyrunapplication.feature.match.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import online.partyrun.partyrunapplication.feature.match.AnimationGaugeBar
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R

@Composable
fun MatchDecisionDialog(
    setShowDialog: (Boolean) -> Unit,
    matchUiState: MatchUiState,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            setShowDialog(false)
        }
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = matchUiState.matchProgress.name,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { setShowDialog(false) }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.completed_matching), fontSize = 20.sp)
                    Text(text = stringResource(id = R.string.ready_to_run_desc), fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(30.dp))
                    AnimationGaugeBar(Color.Red)
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onAccept() },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .size(width = 100.dp, height = 50.dp)
                    ) {
                        Text(text = stringResource(id = R.string.accept_button_title))
                    }
                    Button(
                        onClick = { onDecline() },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .size(width = 100.dp, height = 50.dp)
                    ) {
                        Text(text = stringResource(id = R.string.decline_button_title))
                    }
                }
            }
        }
    }
}
