package online.partyrun.partyrunapplication.feature.my_page.profile

data class ProfileUiState(
    val nickName: String = "",
    val profileImage: String = "",
    val newNickName: String = "",
    val nickNameError: Boolean = false,
    val nickNameSupportingText: String = "",
    val isProfileUpdateSuccess: Boolean = false
)
