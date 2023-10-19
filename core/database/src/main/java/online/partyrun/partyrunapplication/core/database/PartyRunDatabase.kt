package online.partyrun.partyrunapplication.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import online.partyrun.partyrunapplication.core.database.dao.BattleRunningHistoryDao
import online.partyrun.partyrunapplication.core.database.dao.SingleRunningHistoryDao
import online.partyrun.partyrunapplication.core.database.model.BattleRunningHistoryEntity
import online.partyrun.partyrunapplication.core.database.model.SingleRunningHistoryEntity

@Database(
    entities = [SingleRunningHistoryEntity::class, BattleRunningHistoryEntity::class],
    version = 1
)
abstract class PartyRunDatabase : RoomDatabase() {

    abstract fun singleRunningHistoryDao(): SingleRunningHistoryDao

    abstract fun battleRunningHistoryDao(): BattleRunningHistoryDao
}
