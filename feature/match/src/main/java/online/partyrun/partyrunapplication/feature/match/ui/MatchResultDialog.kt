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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState

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
                    Text(text = "다른 유저 매칭 여부 확인 중 ")
                    Spacer(modifier = Modifier.size(30.dp))
                    Text(text = "박현준 : 수락 여부 대기 중")
                    Text(text = "박성우 : 수락")
                    Text(text = "노준혁 : 수락 여부 대기 중")
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "트랙 생성 여부: ${matchUiState.matchResultEventState.status.name}")
                    Text(text = "메세지: ${matchUiState.matchResultEventState.location}")
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
                        Text(text = "취소")
                    }
                }
            }
        }
    }
}
