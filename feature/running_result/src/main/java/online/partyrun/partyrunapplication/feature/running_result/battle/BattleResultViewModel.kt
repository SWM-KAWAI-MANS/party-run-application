package online.partyrun.partyrunapplication.feature.running_result.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.running.battle.GetBattleStatusUseCase
import online.partyrun.partyrunapplication.core.domain.running.battle.GetUserIdUseCase
import online.partyrun.partyrunapplication.core.domain.running_result.GetBattleResultUseCase
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleResultUiModel
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BattleResultViewModel @Inject constructor(
    private val getBattleResultUseCase: GetBattleResultUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getBattleStatusUseCase: GetBattleStatusUseCase
) : ViewModel() {

    private val _battleResultUiState =
        MutableStateFlow<BattleResultUiState>(BattleResultUiState.Loading)
    val battleResultUiState = _battleResultUiState.asStateFlow()

    init {
        getBattleResult()
    }

    fun getBattleResult() {
        viewModelScope.launch {
            val battleData = getBattleStatusUseCase()
            val userId = getUserIdUseCase()

            getBattleResultUseCase().collect { result ->
                result.onSuccess { data ->
                    // 여기서 battleData의 runnerId와 battleResult의 id를 비교하여 name과 profile을 매핑
                    val battleResultStatus = mappingRunnerInfo(
                        data = data,
                        battleData = battleData
                    )

                    _battleResultUiState.value = BattleResultUiState.Success(
                        battleResult = data.copy(
                            userId = userId,
                            battleRunnerStatus = battleResultStatus
                        )
                    )
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    _battleResultUiState.value =
                        BattleResultUiState.LoadFailed
                }
            }
        }
    }

    private fun mappingRunnerInfo(
        data: BattleResultUiModel,
        battleData: BattleStatus
    ): List<RunnerStatusUiModel> {
        val battleResultStatusUiModel = data.battleRunnerStatus.map { battleRunnerStatus ->
            val runnerStatus =
                battleData.battleInfo.find { runnerStatus -> runnerStatus.runnerId == battleRunnerStatus.id }
            battleRunnerStatus.copy(
                name = runnerStatus?.runnerName ?: battleRunnerStatus.name,
                profile = runnerStatus?.runnerProfile ?: battleRunnerStatus.profile
            )
        }
        return battleResultStatusUiModel
    }

}
