package online.partyrun.partyrunapplication.feature.battle

enum class KmState {
    KM_1, KM_3, KM_5, KM_10
}

fun KmState.toDistance(): Int = when (this) {
    KmState.KM_1 -> 1000
    KmState.KM_3 -> 3000
    KmState.KM_5 -> 5000
    KmState.KM_10 -> 10000
}


sealed class BattleUiState {
    object Loading : BattleUiState()

    object Success : BattleUiState()

    object LoadFailed : BattleUiState()

}
