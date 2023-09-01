package online.partyrun.partyrunapplication.feature.my_page.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onEmpty
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.member.GetUserDataUseCase
import online.partyrun.partyrunapplication.core.domain.member.SaveUserDataUseCase
import online.partyrun.partyrunapplication.core.domain.member.UpdateUserDataUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun setNickName(nickName: String) {
        _profileUiState.update {
            it.copy(nickName = nickName)
        }
        Timber.tag("ProfileViewModel").d(_profileUiState.value.nickName)
    }

    private fun isNickNameEmpty(): Boolean {
        val condition = _profileUiState.value.nickName.isEmpty()
        return getResultOfNickNameCondition(
            condition,
            ProfileErrorState.PROFILE_EMPTY
        )
    }

    private fun isInvalidNickNameLength(minLen: Int = 1, maxLen: Int = 6): Boolean {
        val length = _profileUiState.value.nickName.length
        val condition = length < minLen || length > maxLen
        return getResultOfNickNameCondition(
            condition,
            ProfileErrorState.PROFILE_LENGTH_NOT_SATISFIED
        )
    }

    // 조건을 만족하면 true, 아니면 false 반환
    private fun getResultOfNickNameCondition(condition: Boolean, errorState: String): Boolean {
        _profileUiState.update {
            it.copy(
                nickNameError = condition,
                nickNameSupportingText = if (condition) errorState else ""
            )
        }
        return condition
    }

    private fun passAllConditions(): Boolean {
        return !isNickNameEmpty() && !isInvalidNickNameLength()
    }

    fun updateUserData() = viewModelScope.launch {
        if (passAllConditions()) {
            updateUserDataUseCase(
                name = _profileUiState.value.nickName
            ).collect { result ->
                result.onEmpty {
                    saveUserData()
                    _profileUiState.update {
                        it.copy(
                            isProfileUpdateSuccess = true
                        )
                    }
                }.onFailure { errorMessage, code ->
                    _snackbarMessage.value = "변경에 실패하였습니다.\n다시 시도해주세요."
                }
            }
        }
    }

    private suspend fun saveUserData() {
        getUserDataUseCase().collect { result ->
            result.onSuccess { userData ->
                saveUserDataUseCase(userData)
            }.onFailure { errorMessage, code ->
                Timber.e("$code $errorMessage")
            }
        }
    }
}
