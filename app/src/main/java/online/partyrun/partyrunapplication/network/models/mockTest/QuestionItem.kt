package online.partyrun.partyrunapplication.network.models.mockTest

data class QuestionItem(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)