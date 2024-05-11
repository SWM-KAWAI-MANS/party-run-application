package online.partyrun.partyrunapplication.feature.running_result.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.my_page.UpdateSingleRunningHistoryUseCase
import online.partyrun.partyrunapplication.core.domain.running_result.GetSingleResultUseCase
import online.partyrun.partyrunapplication.core.model.running_result.single.toUiModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SingleResultViewModel @Inject constructor(
    private val getsSingleResultUseCase: GetSingleResultUseCase,
    private val updateSingleRunningHistoryUseCase: UpdateSingleRunningHistoryUseCase,
) : ViewModel() {
    private val _singleResultUiState =
        MutableStateFlow<SingleResultUiState>(SingleResultUiState.Loading)
    val singleResultUiState = _singleResultUiState.asStateFlow()

    init {
        getSingleResult()
    }

    fun updateSingleHistory() = viewModelScope.launch {
        updateSingleRunningHistoryUseCase().collect { result ->
            result.onSuccess {
                Timber.d("updateSingleHistory() 성공")
            }.onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
            }
        }
    }

    fun getSingleResult() {
        viewModelScope.launch {
            getsSingleResultUseCase()
                .onSuccess { data ->
                    _singleResultUiState.value = SingleResultUiState.Success(
                        singleResult = data.toUiModel()
                    )
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    _singleResultUiState.value =
                        SingleResultUiState.LoadFailed
                }
        }
    }

}
