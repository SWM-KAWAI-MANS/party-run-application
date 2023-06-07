package online.partyrun.partyrunapplication.presentation.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.partyrun.partyrunapplication.data.model.TestQuestionItem

@Composable
fun TestScreen(
    viewModel: TestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Content(uiState)
}

@Composable
fun Content(
    state: TestState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val dataSet: List<TestQuestionItem> = state.dataSet

        LazyColumn {
            item {
                Text(text = "Test Screen")
            }
            items(dataSet) { item ->
                QuestionCard(item = item)
            }
        }

    }
}

@Composable
fun QuestionCard(item: TestQuestionItem) {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                text = item.question,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = item.answer
            )
        }
    }
}
