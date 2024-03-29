package online.partyrun.partyrunapplication.core.navigation.main

/**
 * 경로 문자열을 Composable() 메소드에 직접 입력하는 것 대신
 * sealed 클래스 활용 -> 유연한 경로 정의
 */
sealed class MainNavRoutes(val route: String) {
    object Battle : MainNavRoutes("battle")
    object Single : MainNavRoutes("single")
    object Challenge : MainNavRoutes("challenge")
    object Party : MainNavRoutes("party")
    object MyPage : MainNavRoutes("myPage")
    object Profile : MainNavRoutes("profile")
    object BattleRunning : MainNavRoutes("battle_running")
    object SingleRunning : MainNavRoutes("single_running")
    object BattleResult : MainNavRoutes("battle_result")
    object SingleResult : MainNavRoutes("single_result")
    object Settings : MainNavRoutes("settings")

}
