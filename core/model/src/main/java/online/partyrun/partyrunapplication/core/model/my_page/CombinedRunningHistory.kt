package online.partyrun.partyrunapplication.core.model.my_page

data class CombinedRunningHistory(
    val singleRunningHistory: SingleRunningHistory = SingleRunningHistory(emptyList()),
    val battleRunningHistory: BattleRunningHistory = BattleRunningHistory(emptyList())
)
