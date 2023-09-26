package online.partyrun.partyrunapplication.core.navigation.main

import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.BattleIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.MyPageIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.PartyIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedBattleIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedMyPageIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedPartyIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedSingleIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SingleIcon
import online.partyrun.partyrunapplication.core.navigation.R

object BottomNavBarItems {
    val BottomBarItems = listOf(
        BottomBarItem(
            title = R.string.battle_title,
            image = BattleIcon,
            selectedImage = SelectedBattleIcon,
            route = MainNavRoutes.Battle.route,
            isLaunched = true
        ),
        BottomBarItem(
            title = R.string.single_title,
            image = SingleIcon,
            selectedImage = SelectedSingleIcon,
            route = MainNavRoutes.Single.route,
            isLaunched = true
        ),
        BottomBarItem(
            title = R.string.party_title,
            image = PartyIcon,
            selectedImage = SelectedPartyIcon,
            route = MainNavRoutes.Party.route,
            isLaunched = true
        ),
        BottomBarItem(
            title = R.string.my_page_title,
            image = MyPageIcon,
            selectedImage = SelectedMyPageIcon,
            route = MainNavRoutes.MyPage.route,
            isLaunched = true
        ),
    )
}
