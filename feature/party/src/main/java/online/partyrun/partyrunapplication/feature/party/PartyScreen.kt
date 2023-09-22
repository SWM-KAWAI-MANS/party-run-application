package online.partyrun.partyrunapplication.feature.party

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.feature.party.ui.PartyJoinDialog

@Composable
fun PartyScreen(
    modifier: Modifier = Modifier,
    navigateToPartyCreation: () -> Unit,
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    Content(
        modifier = modifier,
        partyViewModel = partyViewModel,
        navigateToPartyCreation = navigateToPartyCreation
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    partyViewModel: PartyViewModel,
    navigateToPartyCreation: () -> Unit
) {
    val showJoinDialog = remember { mutableStateOf(false) }

    if (showJoinDialog.value) {
        PartyJoinDialog(
            onDismissRequest = {
                showJoinDialog.value = false
            },
            partyViewModel = partyViewModel
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PartyHeadline()
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PartyRunGradientButton(
                onClick = {
                    showJoinDialog.value = true
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = PartyRunIcons.PartyJoinIcon),
                        contentDescription = stringResource(id = R.string.ic_join_desc)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.join_party),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            SurfaceRoundedRect(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TrackImagePager { index ->
                            partyViewModel.setKmState(KmState.values()[index])
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        onClick = { navigateToPartyCreation() }
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = PartyRunIcons.PartyCreationIcon),
                                contentDescription = stringResource(id = R.string.ic_creation_desc)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(id = R.string.create_party),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun PartyHeadline() {
    HeadLine(
        modifier = Modifier
    ) {
        Text(
            text = stringResource(id = R.string.party_head_line_1),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = stringResource(id = R.string.party_head_line_2),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TrackImagePager(
    setKmState: (Int) -> Unit
) {
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            pageCount = KmState.values().size,
            state = pagerState,
            key = { KmState.values()[it] },
            pageSize = PageSize.Fill
        ) { index ->
            LaunchedEffect(pagerState.currentPage) { // 페이지가 완전히 넘어갔을 경우에만 setKmState
                if (index == pagerState.currentPage) {
                    setKmState(index)
                }
            }
            TrackImage(currentKmState = KmState.values()[index])
        }
        Spacer(modifier = Modifier.height(10.dp))
        // Add a page indicator
        Row(
            Modifier.weight(0.1f),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(KmState.values().size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
private fun TrackImage(currentKmState: KmState) {
    Image(
        painter = painterResource(id = currentKmState.imageRes),
        contentDescription = stringResource(id = R.string.track_image_desc)
    )
}
