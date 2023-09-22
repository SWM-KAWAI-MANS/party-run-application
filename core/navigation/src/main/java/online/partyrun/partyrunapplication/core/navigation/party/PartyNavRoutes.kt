package online.partyrun.partyrunapplication.core.navigation.party

sealed class PartyNavRoutes(val route: String) {
    object PartyRoom: PartyNavRoutes("party_room")

}
