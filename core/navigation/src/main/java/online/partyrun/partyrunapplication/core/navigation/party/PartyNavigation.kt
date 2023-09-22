package online.partyrun.partyrunapplication.core.navigation.party

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.party.ui.PartyCreationScreen
import online.partyrun.partyrunapplication.feature.party.PartyScreen

fun NavGraphBuilder.partyRoute(
    navigateToPartyCreation: () -> Unit,
    navigateToParty: () -> Unit
) {
    composable(route = MainNavRoutes.Party.route) {
        PartyScreen(
            navigateToPartyCreation = navigateToPartyCreation
        )
    }

    // PartyNavRoute
    composable(route = PartyNavRoutes.PartyCreation.route) {
        PartyCreationScreen(
            navigateToParty = navigateToParty
        )
    }
}
