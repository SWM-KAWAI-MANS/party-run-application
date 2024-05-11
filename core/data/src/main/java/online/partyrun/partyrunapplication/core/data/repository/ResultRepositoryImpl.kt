package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.onException
import online.partyrun.partyrunapplication.core.common.network.onSuccess
import online.partyrun.partyrunapplication.core.datastore.datasource.BattlePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.running_result.battle.BattleResult
import online.partyrun.partyrunapplication.core.network.datasource.ResultDataSource
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.toResultModel
import online.partyrun.partyrunapplication.core.database.dao.BattleRunningHistoryDao
import online.partyrun.partyrunapplication.core.database.dao.SingleRunningHistoryDao
import online.partyrun.partyrunapplication.core.database.model.toDomainModel
import online.partyrun.partyrunapplication.core.datastore.datasource.SinglePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.my_page.BattleRunningHistory
import online.partyrun.partyrunapplication.core.model.my_page.ComprehensiveRunRecord
import online.partyrun.partyrunapplication.core.model.my_page.SingleRunningHistory
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult
import online.partyrun.partyrunapplication.core.network.model.response.toEntity
import javax.inject.Inject

class ResultRepositoryImpl @Inject constructor(
    private val resultDataSource: ResultDataSource,
    private val singleRunningHistoryDao: SingleRunningHistoryDao,
    private val battleRunningHistoryDao: BattleRunningHistoryDao,
    private val battlePreferencesDataSource: BattlePreferencesDataSource,
    private val singlePreferencesDataSource: SinglePreferencesDataSource,
) : ResultRepository {
    companion object {
        const val UNKNOWN_ERROR = "Unknown Error"
    }

    override suspend fun getBattleResults(): Result<BattleResult> {
        val battleId = battlePreferencesDataSource.battleData.first().battleId
        return resultDataSource
            .getBattleResults(battleId)
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun getSingleResults(): Result<SingleResult> {
        val singleId = singlePreferencesDataSource
            .getSingleId().first().orEmpty()
        return resultDataSource
            .getSingleResults(singleId)
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun getComprehensiveRunRecord(): Result<ComprehensiveRunRecord> {
        return resultDataSource
            .getComprehensiveRunRecord()
            .toResultModel { it.toDomainModel() }
    }

    override fun updateSingleHistory(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val response = resultDataSource.getSingleHistory()
        response.onSuccess { result ->
            singleRunningHistoryDao.insertAllSingleRunningHistory(result.toEntity())
            emit(Result.Success(Unit))
        }.onException { e ->
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR, -1))
        }
    }

    override fun updateBattleHistory(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val response = resultDataSource.getBattleHistory()
        response.onSuccess { result ->
            battleRunningHistoryDao.insertAllBattleRunningHistory(result.toEntity())
            emit(Result.Success(Unit))
        }.onException { e ->
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR, -1))
        }
    }

    override fun getSingleHistory(): Flow<Result<SingleRunningHistory>> {
        return singleRunningHistoryDao.getAllSingleRunningHistories()
            .map { entityList ->
                try {
                    Result.Success(SingleRunningHistory(entityList.map { it.toDomainModel() }))
                } catch (e: Exception) {
                    Result.Failure(e.message ?: UNKNOWN_ERROR, -1)
                }
            }
    }

    override fun getBattleHistory(): Flow<Result<BattleRunningHistory>> {
        return battleRunningHistoryDao.getAllBattleRunningHistories()
            .map { entityList ->
                try {
                    Result.Success(BattleRunningHistory(entityList.map { it.toDomainModel() }))
                } catch (e: Exception) {
                    Result.Failure(e.message ?: UNKNOWN_ERROR, -1)
                }
            }
    }

    override fun deleteAllHistories(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            singleRunningHistoryDao.deleteAllSingleRunningHistories()
            battleRunningHistoryDao.deleteAllBattleRunningHistories()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR, -1))
        }
    }

}
