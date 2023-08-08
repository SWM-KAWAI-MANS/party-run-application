package online.partyrun.partyrunapplication.feature.running_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.running_result.GetBattleResultUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RunningResultViewModel @Inject constructor(
    private val getBattleResultUseCase: GetBattleResultUseCase
) : ViewModel() {
    private val battleId: String = "64d1d17144c33923f53ec872"

    private val _runningResultUiState = MutableStateFlow(RunningResultUiState.Loading)
    val runningResultUiState = _runningResultUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getBattleResultUseCase(battleId = battleId).collect {
                when(it) {
                    is ApiResponse.Success -> {
                        RunningResultUiState.Success(
                            battleResult = it.data
                        )
                    }
                    is ApiResponse.Failure -> {
                        Timber.e("$it")
                    }
                    ApiResponse.Loading -> {
                        RunningResultUiState.Loading
                    }
                }
            }
        }
    }

}
