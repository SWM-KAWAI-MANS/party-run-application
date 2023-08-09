package online.partyrun.partyrunapplication.feature.my_page

import online.partyrun.partyrunapplication.core.model.user.User

sealed class MyPageUiState {
    object Loading : MyPageUiState()

    data class Success(
        val user: User = User(
            id = "",
            name = "",
            profile = ""
        )
    ) : MyPageUiState()

    object LoadFailed : MyPageUiState()
}