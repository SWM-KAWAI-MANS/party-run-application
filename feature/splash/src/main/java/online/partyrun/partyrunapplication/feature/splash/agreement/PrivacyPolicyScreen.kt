package online.partyrun.partyrunapplication.feature.splash.agreement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.ui.AgreementTopAppBar
import online.partyrun.partyrunapplication.feature.splash.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    modifier: Modifier = Modifier,
    backToAgreementScreen: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AgreementTopAppBar(
                modifier = modifier,
                action = backToAgreementScreen
            ) {
                Text(text = stringResource(id = R.string.privacy_policy_title))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            ShowPrivacyPolicy()
        }
    }
}

@Composable
fun ShowPrivacyPolicy() {
    val policyTitles = listOf(
        R.string.privacy_policy_title_1,
        R.string.privacy_policy_title_2,
        R.string.privacy_policy_title_3,
        R.string.privacy_policy_title_4,
        R.string.privacy_policy_title_5,
        R.string.privacy_policy_title_6,
        R.string.privacy_policy_title_7,
        R.string.privacy_policy_title_8,
        R.string.privacy_policy_title_9,
        R.string.privacy_policy_title_10
    )

    val policyContents = listOf(
        R.string.privacy_policy_content_1,
        R.string.privacy_policy_content_2,
        R.string.privacy_policy_content_3,
        R.string.privacy_policy_content_4,
        R.string.privacy_policy_content_5,
        R.string.privacy_policy_content_6,
        R.string.privacy_policy_content_7,
        R.string.privacy_policy_content_8,
        R.string.privacy_policy_content_9,
        R.string.privacy_policy_content_10
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        itemsIndexed(policyTitles) { index, titleId ->
            Text(text = stringResource(id = titleId), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(id = policyContents[index]), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.size(15.dp))
        }
    }
}
