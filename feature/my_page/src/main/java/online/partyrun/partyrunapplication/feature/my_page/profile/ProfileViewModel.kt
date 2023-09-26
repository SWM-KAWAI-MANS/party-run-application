package online.partyrun.partyrunapplication.feature.my_page.profile

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import online.partyrun.partyrunapplication.core.common.result.onEmpty
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.member.GetUserDataUseCase
import online.partyrun.partyrunapplication.core.domain.member.SaveUserDataUseCase
import online.partyrun.partyrunapplication.core.domain.member.UpdateProfileImageUseCase
import online.partyrun.partyrunapplication.core.domain.member.UpdateUserDataUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val _updateProgressState = MutableStateFlow(false)
    val updateProgressState = _updateProgressState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun initProfileContent(name: String, profileImage: String) {
        _profileUiState.update { state ->
            state.copy(
                nickName = name,
                profileImage = profileImage
            )
        }
    }

    fun setNickName(nickName: String) {
        _profileUiState.update { state ->
            state.copy(newNickName = nickName)
        }
        Timber.tag("ProfileViewModel").d(_profileUiState.value.newNickName)
    }

    private fun isNickNameEmpty(): Boolean {
        val condition = _profileUiState.value.newNickName.isEmpty()
        return getResultOfNickNameCondition(
            condition,
            ProfileErrorState.PROFILE_EMPTY
        )
    }

    private fun isInvalidNickNameLength(minLen: Int = 1, maxLen: Int = 6): Boolean {
        val length = _profileUiState.value.newNickName.length
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

    fun handlePickedImage(context: Context, uri: Uri) {
        _updateProgressState.value = true
        val requestBody = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
        }
        val fileName = getFileName(context, uri)
        requestBody?.let {
            updateProfileImage(requestBody, fileName)
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }

    fun updateUserData() = viewModelScope.launch {
        _updateProgressState.value = true
        if (passAllConditions()) {
            updateUserDataUseCase(
                nickName = _profileUiState.value.newNickName
            ).collect { result ->
                result.onEmpty {
                    saveUserData()
                    _profileUiState.update { state ->
                        state.copy(
                            isProfileUpdateSuccess = true
                        )
                    }
                    _updateProgressState.value = false
                    _snackbarMessage.value = "프로필 정보를 변경하였습니다."
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    _updateProgressState.value = false
                    _snackbarMessage.value = "변경에 실패하였습니다.\n다시 시도해주세요."
                }
            }
        }
    }

    private fun updateProfileImage(requestBody: RequestBody, fileName: String?) =
        viewModelScope.launch {
            updateProfileImageUseCase(
                requestBody = requestBody,
                fileName = fileName
            ).collect { result ->
                result.onEmpty {
                    saveUserData()
                    _updateProgressState.value = false
                    _snackbarMessage.value = "프로필 사진이 변경되었습니다."
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    _updateProgressState.value = false
                    _snackbarMessage.value = "변경에 실패하였습니다.\n다시 시도해주세요."
                }
            }
        }

    private suspend fun saveUserData() {
        getUserDataUseCase().collect { result ->
            result.onSuccess { userData ->
                saveUserDataUseCase(userData)
                _profileUiState.update { state ->
                    state.copy(
                        nickName = userData.nickName,
                        profileImage = userData.profileImage
                    )
                }
            }.onFailure { errorMessage, code ->
                Timber.e("$code $errorMessage")
            }
        }
    }
}
