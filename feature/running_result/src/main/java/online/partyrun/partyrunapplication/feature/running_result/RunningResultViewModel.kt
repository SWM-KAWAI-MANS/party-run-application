package online.partyrun.partyrunapplication.feature.running_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.running.GetBattleStatusUseCase
import online.partyrun.partyrunapplication.core.domain.running.GetUserIdUseCase
import online.partyrun.partyrunapplication.core.domain.running_result.GetBattleResultUseCase
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.model.running_result.BattleRunnerStatus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RunningResultViewModel @Inject constructor(
    private val getBattleResultUseCase: GetBattleResultUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getBattleStatusUseCase: GetBattleStatusUseCase
) : ViewModel() {

    private lateinit var userId: String
    private val _runningResultUiState = MutableStateFlow<RunningResultUiState>(RunningResultUiState.Loading)
    val runningResultUiState = _runningResultUiState.asStateFlow()
    init {
        getBattleResult()
        getUserId()
    }

    private fun getUserId() {
        viewModelScope.launch {
            userId = getUserIdUseCase()
            _runningResultUiState.update { state ->
                when (state) {
                    is RunningResultUiState.Success -> state.copy(battleResult = state.battleResult.copy(userId = userId))
                    else -> state
                }
            }
        }
    }

    private fun getBattleResult() {
        viewModelScope.launch {
            val battleData = getBattleStatusUseCase()

            getBattleResultUseCase().collect {
                when (it) {
                    is ApiResponse.Success -> {
                        // 여기서 battleData의 runnerId와 battleResult의 id를 비교하여 name과 profile을 매핑
                        val battleResultStatus = mappingRunnerInfo(it, battleData)

                        _runningResultUiState.value = RunningResultUiState.Success(
                            battleResult = it.data.copy(
                                battleRunnerStatus = battleResultStatus
                            )
                        )
                    }

                    is ApiResponse.Failure -> {
                        Timber.e("$it")
                        _runningResultUiState.value =
                            RunningResultUiState.LoadFailed
                    }

                    ApiResponse.Loading -> {
                        _runningResultUiState.value =
                            RunningResultUiState.Loading
                    }
                }
            }
        }
    }

    private fun mappingRunnerInfo(
        it: ApiResponse.Success<BattleResult>,
        battleData: BattleStatus
    ): List<BattleRunnerStatus> {
        val battleResultStatus = it.data.battleRunnerStatus.map { battleRunnerStatus ->
            val runnerStatus =
                battleData.battleInfo.find { runnerStatus -> runnerStatus.runnerId == battleRunnerStatus.id }
            battleRunnerStatus.copy(
                name = runnerStatus?.runnerName ?: battleRunnerStatus.name,
                profile = runnerStatus?.runnerProfile ?: battleRunnerStatus.profile
            )
        }
        return battleResultStatus
    }


}
