package online.partyrun.partyrunapplication.feature.my_page.profile

data class ProfileUiState(
    val nickName: String = "",
    val nickNameError: Boolean = false,
    val nickNameSupportingText: String = "",
)