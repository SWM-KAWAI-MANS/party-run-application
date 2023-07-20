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
fun TermsOfServiceScreen(
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
                Text(text = stringResource(id = R.string.terms_of_services_title))
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
            ShowTermOfService()
        }
    }
}

@Composable
fun ShowTermOfService() {
    val policyTitles = listOf(
        R.string.term_of_service_title_1,
        R.string.term_of_service_title_2,
        R.string.term_of_service_title_3,
        R.string.term_of_service_title_4,
        R.string.term_of_service_title_5,
        R.string.term_of_service_title_6,
        R.string.term_of_service_title_7,
        R.string.term_of_service_title_8,
        R.string.term_of_service_title_9,
        R.string.term_of_service_title_10,
        R.string.term_of_service_title_11,
        R.string.term_of_service_title_12,
        R.string.term_of_service_title_13
    )

    val policyContents = listOf(
        R.string.term_of_service_content_1,
        R.string.term_of_service_content_2,
        R.string.term_of_service_content_3,
        R.string.term_of_service_content_4,
        R.string.term_of_service_content_5,
        R.string.term_of_service_content_6,
        R.string.term_of_service_content_7,
        R.string.term_of_service_content_8,
        R.string.term_of_service_content_9,
        R.string.term_of_service_content_10,
        R.string.term_of_service_content_11,
        R.string.term_of_service_content_12,
        R.string.term_of_service_content_13
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        itemsIndexed(policyTitles) { index, titleId ->
            Text(text = stringResource(id = titleId), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(id = policyContents[index]), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.size(15.dp))
        }
    }
}
