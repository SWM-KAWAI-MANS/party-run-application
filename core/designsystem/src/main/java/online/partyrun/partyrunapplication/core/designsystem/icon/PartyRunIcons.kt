package online.partyrun.partyrunapplication.core.designsystem.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import online.partyrun.partyrunapplication.core.designsystem.R
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.BattleIcon
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.SingleIcon

object PartyRunIcons {
    val ArrowBack = R.drawable.ic_round_arrow_back_24
    val Search = R.drawable.ic_round_search_24
    val ArrowBackIos = R.drawable.ic_round_arrow_back_ios_new_24
    val ArrowForwardIos = R.drawable.ic_round_arrow_forward_ios_24
    val ExpandMore = R.drawable.ic_round_expand_more_24
    val ExpandLess = R.drawable.ic_round_expand_less_24
    val Menu = R.drawable.ic_round_menu_24
    val Close = R.drawable.ic_round_close_24
    val Add = R.drawable.ic_round_add_24
    val Remove = R.drawable.ic_round_remove_24
    val CheckCircle = R.drawable.ic_round_check_circle_outline_24
    val AddCircle = R.drawable.ic_round_add_circle_outline_24
    val RemoveCircle = R.drawable.ic_round_remove_circle_outline_24
    val ErrorOutline = R.drawable.ic_round_error_outline_24
    val Circle = R.drawable.ic_outline_circle_24
    val RadioButtonChecked = R.drawable.ic_radio_button_checked
    val VolumeOn = R.drawable.ic_outline_volume_up_24
    val VolumeOff = R.drawable.ic_outline_volume_off_24
    val Settings = R.drawable.ic_outline_settings_24
    val Step = R.drawable.ic_outline_step
    val Pace = R.drawable.ic_outline_pace
    val Schedule = R.drawable.ic_outline_schedule
    val CheckCircleFilled = R.drawable.ic_round_check_circle_24
    val PauseCircleFilled = R.drawable.ic_round_pause_circle_filled_24
    val PlayCircleFilled = R.drawable.ic_round_play_circle_filled_24
    val CancelFilled = R.drawable.ic_round_cancel_24
    val VolumeOnFilled = R.drawable.ic_baseline_volume_up_24
    val VolumeOffFilled = R.drawable.ic_baseline_volume_off_24
    val ArrowCircleUp = R.drawable.ic_round_arrow_circle_up_24
    val Checked = R.drawable.ic_checked
    val UnChecked = R.drawable.ic_unchecked
    val BattleIcon = R.drawable.ic_battle
    val SelectedBattleIcon = R.drawable.ic_selected_battle
    val SingleIcon = R.drawable.ic_single
    val SelectedSingleIcon = R.drawable.ic_selected_single
    val ChallengeIcon = R.drawable.ic_challenge
    val SelectedChallengeIcon = R.drawable.ic_selected_challenge
    val MyPageIcon = R.drawable.ic_my_page
    val SelectedMyPageIcon = R.drawable.ic_selected_my_page
    val DistanceIcon = R.drawable.ic_rounded_place
    val edit = R.drawable.ic_round_edit_24
}

@Preview(showBackground = true)
@Composable
fun PartyRunMainPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ImagePreview(image = BattleIcon)
        ImagePreview(image = SingleIcon)
    }
}

@Composable
private fun ImagePreview(
    image: Int
) {
    val painter: Painter = painterResource(image)
    Image(painter = painter, contentDescription = null)
}
