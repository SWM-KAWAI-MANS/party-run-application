package online.partyrun.partyrunapplication.feature.running_result.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.running_result.GetStoredSingleResultUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SingleResultViewModel @Inject constructor(
    private val getStoredSingleResultUseCase: GetStoredSingleResultUseCase
) : ViewModel() {
    private val _singleResultUiState =
        MutableStateFlow<SingleResultUiState>(SingleResultUiState.Loading)
    val singleResultUiState = _singleResultUiState.asStateFlow()

    init {
        getSingleResult()
    }

    fun getSingleResult() {
        viewModelScope.launch {
            try {
                _singleResultUiState.value = SingleResultUiState.Success(
                    singleResult = getStoredSingleResultUseCase()
                )
            } catch (e: Exception) {
                Timber.tag("getSingleResult 에러").e(e)
                _singleResultUiState.value =
                    SingleResultUiState.LoadFailed
            }
        }
    }

}

