package online.partyrun.partyrunapplication.core.navigation.main

data class BottomBarItem(
    val title: Int,
    val image: Int,
    val selectedImage: Int,
    val route: String,
    val isLaunched : Boolean
)
