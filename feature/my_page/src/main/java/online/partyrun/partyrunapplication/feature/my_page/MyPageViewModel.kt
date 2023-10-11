package online.partyrun.partyrunapplication.feature.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.my_page.GetComprehensiveRunRecordUseCase
import online.partyrun.partyrunapplication.core.domain.my_page.GetMyPageDataUseCase
import online.partyrun.partyrunapplication.core.domain.my_page.GetRunningHistoryUseCase
import online.partyrun.partyrunapplication.core.domain.running.battle.SaveBattleIdUseCase
import online.partyrun.partyrunapplication.core.domain.running.single.SaveSingleIdUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageDataUseCase: GetMyPageDataUseCase,
    private val getComprehensiveRunRecordUseCase: GetComprehensiveRunRecordUseCase,
    private val getRunningHistoryUseCase: GetRunningHistoryUseCase,
    private val saveSingleIdUseCase: SaveSingleIdUseCase,
    private val saveBattleIdUseCase: SaveBattleIdUseCase
) : ViewModel() {
    private val _myPageProfileState =
        MutableStateFlow<MyPageProfileState>(MyPageProfileState.Loading)
    val myPageProfileState = _myPageProfileState.asStateFlow()

    private val _myPageComprehensiveRunRecordState =
        MutableStateFlow<MyPageComprehensiveRunRecordState>(MyPageComprehensiveRunRecordState.Loading)
    val myPageComprehensiveRunRecordState = _myPageComprehensiveRunRecordState.asStateFlow()

    private val _runningHistoryState =
        MutableStateFlow<RunningHistoryState>(RunningHistoryState.Loading)
    val runningHistoryState = _runningHistoryState.asStateFlow()

    val saveIdCompleteEvent = MutableSharedFlow<ModeType>(replay = 0)

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    init {
        getMyPageUserData()
    }

    private fun getMyPageUserData() {
        viewModelScope.launch {
            try {
                val user = getMyPageDataUseCase()
                _myPageProfileState.value = MyPageProfileState.Success(user = user)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun getComprehensiveRunRecord() = viewModelScope.launch {
        _myPageComprehensiveRunRecordState.value =
            MyPageComprehensiveRunRecordState.Loading

        getComprehensiveRunRecordUseCase().collect { result ->
            result.onSuccess {
                _myPageComprehensiveRunRecordState.value =
                    MyPageComprehensiveRunRecordState.Success(
                        comprehensiveRunRecord = it
                    )
            }.onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
                _snackbarMessage.value = "종합기록을 불러오는데\n실패했습니다."
                _myPageComprehensiveRunRecordState.value =
                    MyPageComprehensiveRunRecordState.LoadFailed
            }
        }
    }

    fun getRunningHistory() = viewModelScope.launch {
        getRunningHistoryUseCase().collect { result ->
            result.onSuccess {
                _runningHistoryState.value = RunningHistoryState.Success(
                    combinedRunningHistory = it
                )
            }.onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
                _snackbarMessage.value = "이전 기록을 불러오는데\n실패했습니다."
                _runningHistoryState.value =
                    RunningHistoryState.LoadFailed
            }
        }
    }

    fun saveSingleId(singleId: String) = viewModelScope.launch {
        saveSingleIdUseCase(singleId)
        saveIdCompleteEvent.emit(ModeType.SINGLE)
    }

    fun saveBattleId(battleId: String) = viewModelScope.launch {
        saveBattleIdUseCase(battleId)
        saveIdCompleteEvent.emit(ModeType.BATTLE)
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

}
