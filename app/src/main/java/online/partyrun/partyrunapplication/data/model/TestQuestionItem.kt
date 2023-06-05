package online.partyrun.partyrunapplication.data.model

data class TestQuestionItem(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)