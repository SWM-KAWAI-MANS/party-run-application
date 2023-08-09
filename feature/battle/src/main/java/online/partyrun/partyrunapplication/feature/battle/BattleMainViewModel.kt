package online.partyrun.partyrunapplication.feature.battle

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BattleMainViewModel @Inject constructor(

) : ViewModel() {

    private val _battleMainUiState = MutableStateFlow<BattleMainUiState>(BattleMainUiState.Success)
    val battleMainUiState = _battleMainUiState.asStateFlow()

}
