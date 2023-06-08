package online.partyrun.partyrunapplication.core.model

data class TestQuestionItem(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)