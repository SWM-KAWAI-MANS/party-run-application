package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult

interface BattleDataSource {
    suspend fun getBattleId(): ApiResult<String>
}
