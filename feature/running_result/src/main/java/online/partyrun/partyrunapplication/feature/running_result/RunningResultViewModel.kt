package online.partyrun.partyrunapplication.feature.running_result

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import online.partyrun.partyrunapplication.core.domain.running_result.GetBattleResultUseCase
import javax.inject.Inject

@HiltViewModel
class RunningResultViewModel @Inject constructor(
    private val getBattleResultUseCase: GetBattleResultUseCase
) : ViewModel() {

}
