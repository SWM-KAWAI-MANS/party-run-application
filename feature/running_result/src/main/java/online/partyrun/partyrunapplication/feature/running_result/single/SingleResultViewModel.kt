package online.partyrun.partyrunapplication.feature.running_result.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.running_result.GetSingleResultUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SingleResultViewModel @Inject constructor(
    private val getsSingleResultUseCase: GetSingleResultUseCase
) : ViewModel() {
    private val _singleResultUiState =
        MutableStateFlow<SingleResultUiState>(SingleResultUiState.Loading)
    val singleResultUiState = _singleResultUiState.asStateFlow()

    init {
        getSingleResult()
    }

    fun getSingleResult() {
        viewModelScope.launch {
            getsSingleResultUseCase().collect { result ->
                result.onSuccess { data ->
                    _singleResultUiState.value = SingleResultUiState.Success(
                        singleResult = data
                    )
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    _singleResultUiState.value =
                        SingleResultUiState.LoadFailed
                }
            }
        }
    }

}

