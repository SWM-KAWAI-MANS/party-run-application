package online.partyrun.partyrunapplication.core.navigation.party

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.party.PartyScreen
import online.partyrun.partyrunapplication.feature.party.ui.PartyRoomScreen

fun NavGraphBuilder.partyRoute(
    navigateToPartyRoom: (String) -> Unit,
    navigateToParty: () -> Unit
) {
    composable(route = MainNavRoutes.Party.route) {
        PartyScreen(
            navigateToPartyRoom = navigateToPartyRoom
        )
    }

    // PartyNavRoute
    composable(
        route = "${PartyNavRoutes.PartyRoom.route}?code={code}",
        arguments = listOf(navArgument("code") {
            type = NavType.StringType
            defaultValue = ""
        })
    ) { backStackEntry ->
        PartyRoomScreen(
            partyCode = backStackEntry.arguments?.getString("code"),
            navigateToParty = navigateToParty
        )
    }
}
