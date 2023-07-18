package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.R

@Composable
fun HeadLine(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(35.dp))
        Image(
            modifier = Modifier.width(150.dp),
            painter = painterResource(id = R.drawable.title),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(15.dp))
        content()
    }
}
