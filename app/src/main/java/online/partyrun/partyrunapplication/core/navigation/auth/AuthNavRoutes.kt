package online.partyrun.partyrunapplication.core.navigation.auth

/**
 * 경로 문자열을 Composable() 메소드에 직접 입력하는 것 대신
 * sealed 클래스 활용 -> 유연한 경로 정의
 */
sealed class AuthNavRoutes(val route: String) {
    object Splash: AuthNavRoutes("splash")
    object SignIn: AuthNavRoutes("sign_in")
}