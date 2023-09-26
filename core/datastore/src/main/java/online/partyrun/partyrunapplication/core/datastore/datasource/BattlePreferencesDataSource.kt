package online.partyrun.partyrunapplication.core.datastore.datasource

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.datastore.BattlePreferences
import online.partyrun.partyrunapplication.core.datastore.BattleRunner
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.battle.BattleRunnerDisplayStatus
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import javax.inject.Inject

class BattlePreferencesDataSource @Inject constructor(
    private val battlePreferences: DataStore<BattlePreferences>
) {
    // 매핑을 통해 필요한 대결 멤버 데이터 생성
    val battleData = battlePreferences.data
        .map { preferences ->
            BattleStatus(
                battleId = preferences.battleId,
                battleInfo = preferences.runnersList.map {
                    BattleRunnerDisplayStatus(
                        runnerId = it.id,
                        runnerName = it.name,
                        runnerProfile = it.profile
                    )
                }
            )
        }

    suspend fun setRunners(runnerInfoData: RunnerInfoData) {
        battlePreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .clearRunners()
                .addAllRunners(runnerInfoData.runners.map {
                    BattleRunner.newBuilder()
                        .setId(it.id)
                        .setName(it.name)
                        .setProfile(it.profile)
                        .build()
                })
                .build()
        }
    }

    suspend fun setBattleId(battleId: String) {
        battlePreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setBattleId(battleId)
                .build()
        }
    }
}
