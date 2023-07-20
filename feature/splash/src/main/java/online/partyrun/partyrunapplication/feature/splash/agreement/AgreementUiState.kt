package online.partyrun.partyrunapplication.feature.splash.agreement

data class AgreementUiState(
    val isTermsOfServiceChecked: Boolean = false, // 서비스 이용약관 동의 여부
    val isPrivacyPolicyChecked: Boolean = false, // 개인정보 처리방침 동의 여부
    val isAllChecked: Boolean = false // 사용자가 모든 약관에 동의했는지 여부
)
