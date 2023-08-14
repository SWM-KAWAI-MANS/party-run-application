package online.partyrun.partyrunapplication.core.navigation.main

import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.BattleIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.ChallengeIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.MyPageIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedBattleIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedChallengeIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SelectedMyPageIcon
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
            isLaunched = false
        ),
        BottomBarItem(
            title = R.string.challenge_title,
            image = ChallengeIcon,
            selectedImage = SelectedChallengeIcon,
            route = MainNavRoutes.Challenge.route,
            isLaunched = false
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
