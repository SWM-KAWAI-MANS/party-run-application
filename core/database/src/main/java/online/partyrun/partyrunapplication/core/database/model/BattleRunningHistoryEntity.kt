package online.partyrun.partyrunapplication.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import online.partyrun.partyrunapplication.core.model.my_page.RunningHistoryDetail

@Entity(
    tableName = "battle_running_history_resource",
)
data class BattleRunningHistoryEntity(
    @PrimaryKey
    val id: String,
    val date: String,
    val runningTime: String,
    val distanceFormatted: String
)

fun BattleRunningHistoryEntity.toDomainModel(): RunningHistoryDetail {
    return RunningHistoryDetail(
        id = this.id,
        date = this.date,
        runningTime = this.runningTime,
        distanceFormatted = this.distanceFormatted
    )
}
