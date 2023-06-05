package online.partyrun.partyrunapplication.navigation

/**
 * 경로 문자열을 Composable() 메소드에 직접 입력하는 것 대신
 * sealed 클래스 활용 -> 유연한 경로 정의
 */
sealed class NavRoutes(val route: String) {
    object Test1: NavRoutes("route_test1")
    object Test2: NavRoutes("route_test2")
    object Test3: NavRoutes("route_test3")
    object Test4: NavRoutes("route_test4")
}
