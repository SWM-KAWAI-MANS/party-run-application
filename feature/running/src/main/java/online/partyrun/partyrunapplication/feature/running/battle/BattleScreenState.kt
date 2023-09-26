package online.partyrun.partyrunapplication.feature.running.battle

sealed class BattleScreenState {
    object Ready : BattleScreenState()
    object Running : BattleScreenState()
    object Finish : BattleScreenState()
    object LoadFailed : BattleScreenState()
}
