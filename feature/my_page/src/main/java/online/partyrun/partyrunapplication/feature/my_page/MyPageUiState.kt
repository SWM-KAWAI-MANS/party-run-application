package online.partyrun.partyrunapplication.feature.my_page

import online.partyrun.partyrunapplication.core.model.my_page.ComprehensiveRunRecord
import online.partyrun.partyrunapplication.core.model.my_page.SingleRunningHistory
import online.partyrun.partyrunapplication.core.model.user.User

enum class ModeType {
    SINGLE, BATTLE
}

sealed class MyPageProfileState {
    object Loading : MyPageProfileState()

    data class Success(
        val user: User = User(
            id = "",
            nickName = "",
            profileImage = ""
        )
    ) : MyPageProfileState()

    object LoadFailed : MyPageProfileState()
}

sealed class MyPageComprehensiveRunRecordState {
    object Loading : MyPageComprehensiveRunRecordState()

    data class Success(
        val comprehensiveRunRecord: ComprehensiveRunRecord = ComprehensiveRunRecord(
            averagePace = "0'00''",
            totalDistance = "0km",
            totalRunningTime = "00:00"
        )
    ) : MyPageComprehensiveRunRecordState()

    object LoadFailed : MyPageComprehensiveRunRecordState()
}

sealed class RunningHistoryState {
    object Loading : RunningHistoryState()

    data class Success(
        val singleRunningHistory: SingleRunningHistory = SingleRunningHistory(emptyList())
    ) : RunningHistoryState()

    object LoadFailed : RunningHistoryState()
}
