package online.partyrun.partyrunapplication.feature.splash.agreement

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunAnimatedButton
import online.partyrun.partyrunapplication.core.ui.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.core.ui.LeadingIconAgreementText
import online.partyrun.partyrunapplication.feature.splash.R

@Composable
fun AgreementScreen(
    navigationToTermsOfService: () -> Unit,
    navigationToPrivacyPolicy: () -> Unit,
    navigationToSignIn: () -> Unit,
    agreementViewModel: AgreementViewModel = hiltViewModel()
) {
    val state by agreementViewModel.agreementUiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HeadLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = stringResource(id = R.string.headline_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            LeadingIconAgreementText(
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 10.dp),
                toggleButtonChecked = state.isAllChecked,
                toggleOnCheckedChange = {
                    agreementViewModel.onCheckedChangeAllAgreement()
                }
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.all_agree),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            SurfaceRoundedRect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                color = MaterialTheme.colorScheme.background
            ) {
                TermsRow(
                    conditionChecked = state.isTermsOfServiceChecked,
                    setToggleButtonChecked = {
                        agreementViewModel.setTermsOfServiceChecked()
                    },
                    textId = R.string.terms_of_services_subject,
                ) {
                    navigationToTermsOfService()
                }
                TermsRow(
                    conditionChecked = state.isPrivacyPolicyChecked,
                    setToggleButtonChecked = {
                        agreementViewModel.setPrivacyPolicyChecked()
                    },
                    textId = R.string.privacy_policy_subject,
                ) {
                    navigationToPrivacyPolicy()
                }
            }
            Spacer(modifier = Modifier.size(80.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter)
        ) {
            PartyRunAnimatedButton(
                modifier = Modifier.padding(horizontal = 40.dp),
                onClick = {
                    agreementViewModel.saveAgreementState(isChecked = true)
                    navigationToSignIn()
                },
                visible = state.isAllChecked
            ) {
                Text(
                    text = stringResource(id = R.string.next_step),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun TermsRow(
    conditionChecked: Boolean,
    setToggleButtonChecked: () -> Unit,
    textId: Int,
    navigateToDetailScreen: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LeadingIconAgreementText(
            modifier = Modifier,
            toggleButtonChecked = conditionChecked,
            toggleOnCheckedChange = {
                setToggleButtonChecked()
            }
        ) {
            Text(
                modifier = Modifier.padding(bottom = 3.dp),
                text = stringResource(id = textId),
            )
        }
        TextButton(
            modifier = Modifier.padding(end = 10.dp, bottom = 3.dp),
            onClick = navigateToDetailScreen
        ) {
            Text(
                text = stringResource(id = R.string.read_detail)
            )
        }
    }
}
