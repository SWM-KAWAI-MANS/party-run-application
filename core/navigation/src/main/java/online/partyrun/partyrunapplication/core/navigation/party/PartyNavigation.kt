package online.partyrun.partyrunapplication.core.navigation.party

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.party.PartyScreen

fun NavGraphBuilder.partyRoute() {
    composable(route = MainNavRoutes.Party.route) {
        PartyScreen()
    }
}