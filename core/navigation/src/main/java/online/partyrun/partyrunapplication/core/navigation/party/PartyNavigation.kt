package online.partyrun.partyrunapplication.core.navigation.party

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.party.ui.PartyCreationScreen
import online.partyrun.partyrunapplication.feature.party.PartyScreen

fun NavGraphBuilder.partyRoute(
    navigateToPartyCreation: (String) -> Unit,
    navigateToParty: () -> Unit
) {
    composable(route = MainNavRoutes.Party.route) {
        PartyScreen(
            navigateToPartyCreation = navigateToPartyCreation
        )
    }

    // PartyNavRoute
    composable(
        route = "${PartyNavRoutes.PartyCreation.route}?code={code}",
        arguments = listOf(navArgument("code") {
            type = NavType.StringType
            defaultValue = ""
        })
    ) { backStackEntry ->
        PartyCreationScreen(
            partyCode = backStackEntry.arguments?.getString("code"),
            navigateToParty = navigateToParty
        )
    }
}
