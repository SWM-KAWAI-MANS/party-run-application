package online.partyrun.partyrunapplication.feature.party

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(

) : ViewModel() {
    private val _kmState = MutableStateFlow(KmState.KM_1)

    fun setKmState(state: KmState) {
        _kmState.value = state
    }

}
