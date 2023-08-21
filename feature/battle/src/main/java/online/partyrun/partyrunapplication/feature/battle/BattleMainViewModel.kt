package online.partyrun.partyrunapplication.feature.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.running.TerminateOngoingBattleUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BattleMainViewModel @Inject constructor(
    private val terminateOngoingBattleUseCase: TerminateOngoingBattleUseCase
) : ViewModel() {

    private val _battleMainUiState = MutableStateFlow<BattleMainUiState>(BattleMainUiState.Success)
    val battleMainUiState = _battleMainUiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun onKmChangeButtonClick() = viewModelScope.launch {
        _snackbarMessage.value = "현재 1km만 지원합니다."
    }

    /**
     * 앱이 처음 시작될 때(MainActivity가 onCreate될 때) 진행 중인 배틀이 있다면 종료 요청 수행
     */
    fun terminateOngoingBattle() = viewModelScope.launch {
        terminateOngoingBattleUseCase().collect { result ->
            result.onSuccess {
                Timber.tag("BattleMainViewModel").d("현재 진행 중인 배틀이 있다면 종료 요청 성공")
            }.onFailure { errorMessage, code ->
                _snackbarMessage.value = "기존 대결을 종료할 수 없습니다."
                Timber.tag("BattleMainViewModel").e("$code $errorMessage")
            }
        }
    }

}
