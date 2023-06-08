package online.partyrun.partyrunapplication.core.navigation.main

/**
 * 경로 문자열을 Composable() 메소드에 직접 입력하는 것 대신
 * sealed 클래스 활용 -> 유연한 경로 정의
 */
sealed class MainNavRoutes(val route: String) {
    object Test1: MainNavRoutes("route_test1")
    object Test2: MainNavRoutes("route_test2")
    object Test3: MainNavRoutes("route_test3")
    object Test4: MainNavRoutes("route_test4")
}
