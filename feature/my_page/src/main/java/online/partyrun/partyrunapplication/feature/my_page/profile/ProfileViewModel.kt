package online.partyrun.partyrunapplication.feature.my_page.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

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

    fun passAllConditions(): Boolean {
        return !isNickNameEmpty() && !isInvalidNickNameLength()
    }

}

