package online.partyrun.partyrunapplication.feature.match.ui

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import online.partyrun.partyrunapplication.core.model.match.MatchMember
import online.partyrun.partyrunapplication.core.model.match.PlayerStatus
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R

@Composable
fun MatchResultDialog(
    setShowDialog: (Boolean) -> Unit,
    matchUiState: MatchUiState
) {
    Dialog(
        onDismissRequest = {
            setShowDialog(false)
        }
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .height(600.dp),
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
                    modifier = Modifier
                        .width(300.dp)
                        .height(350.dp),
                ) {
                    Text(text = stringResource(id = R.string.waiting_for_other_desc))
                    Spacer(modifier = Modifier.size(30.dp))
                    DisplayMatchStatus(matchUiState = matchUiState)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { setShowDialog(false) },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                    ) {
                        Text(text = stringResource(id = R.string.cancel_button_title))
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayMatchStatus(
    matchUiState: MatchUiState
) {
    LazyColumn {
        itemsIndexed(
            matchUiState.matchResultEventState.members
        ) { index, item ->
            MatchStatusItem(player = item)
        }
    }
}

@Composable
fun MatchStatusItem(
    player: MatchMember
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Player: ${player.id}")
            OvalBorderedText(
                text = convertPlayerStatus(player.status),
                textColor = Color.Blue,
                borderColor = Color.Red
            )
        }
    }
    Spacer(modifier = Modifier.size(10.dp))
}

@Composable
fun OvalBorderedText(
    text: String,
    textColor: Color,
    borderColor: Color = Color.Red,
    borderWidth: Dp = 2.dp
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50)) // 둥근 모양으로 클리핑
            .border(2.dp, borderColor, RoundedCornerShape(percent = 50)) // 둥근 모양의 테두리를 추가
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor)
    }
}

@Composable
fun convertPlayerStatus(status: String): String {
    return when(status) {
        PlayerStatus.NO_RESPONSE.name -> stringResource(id = R.string.waiting_for_user)
        PlayerStatus.READY.name -> stringResource(id = R.string.accept_matching_status)
        PlayerStatus.CANCELED.name -> stringResource(id = R.string.decline_matching_status)
        else -> stringResource(id = R.string.else_status)
    }
}
