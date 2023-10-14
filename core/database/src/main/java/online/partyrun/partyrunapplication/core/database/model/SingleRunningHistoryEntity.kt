package online.partyrun.partyrunapplication.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import online.partyrun.partyrunapplication.core.model.my_page.RunningHistoryDetail

@Entity(
    tableName = "single_running_history_resource",
)
data class SingleRunningHistoryEntity(
    @PrimaryKey
    val id: String,
    val date: String,
    val runningTime: String,
    val distanceFormatted: String
)

fun SingleRunningHistoryEntity.toDomainModel(): RunningHistoryDetail {
    return RunningHistoryDetail(
        id = this.id,
        date = this.date,
        runningTime = this.runningTime,
        distanceFormatted = this.distanceFormatted
    )
}
