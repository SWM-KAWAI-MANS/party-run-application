package online.partyrun.partyrunapplication.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.di.network.TokenManager
import online.partyrun.partyrunapplication.domain.use_case.TestUseCase
import online.partyrun.partyrunapplication.network.ApiResponse
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val testUseCase: TestUseCase,
    private val tokenManager: TokenManager
): ViewModel() {

    private val _uiState = MutableStateFlow(TestState())
    val uiState: StateFlow<TestState> = _uiState.asStateFlow()

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() = viewModelScope.launch {
        testUseCase.getAllQuestions().collect() {
            when(it) {
                is ApiResponse.Success -> {
                    //Timber.tag("getAllQuestion_ApiSuccess").d("$it")
                    _uiState.update { state ->
                        state.copy(
                            dataSet = it.data
                        )
                    }
                }
                is ApiResponse.Failure -> {
                    Timber.tag("getAllQuestion_ApiFailure").e("${it.code} ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("getAllQuestion_Loading").e("Loading")
                }
            }
        }
    }
}
